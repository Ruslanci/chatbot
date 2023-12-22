package game.telegram;

import static org.telegram.abilitybots.api.objects.Flag.TEXT;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import game.core.DatabaseHandler;
import game.core.GameSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.crypto.Data;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Timer;
import java.util.TimerTask;

public class ChatBot extends AbilityBot {

  private static final String tokenPath = "PasswordGameBotToken";
  private static final String namePath = "PasswordGameBotName";

  /** A hashmap userSessions keeps track of users and their game sessions.
   * It stores (userId, GameSession). */
  private final Map<Long, GameSession> userSessions;
  private final DatabaseHandler database;

  public ChatBot() {
    super(System.getenv(tokenPath), System.getenv(namePath));
    userSessions = new HashMap<>();
    database = new DatabaseHandler();
  }
  public ChatBot(String databaseUrl) {
    super(System.getenv(tokenPath), System.getenv(namePath));
    userSessions = new HashMap<>();
    database = new DatabaseHandler(databaseUrl);
  }

  @Override
  public long creatorId() {
    return 0;
  }

  /** Receives user messages (passwords). If the user session is valid,
  passes on the message to GameSession::onMessageReceived method*/
  public Ability receiveMessage() {
    return Ability.builder()
        .name(DEFAULT)
        .flag(TEXT)
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long userId = ctx.user().getId();
          GameSession session = userSessions.get(userId);

          if (session == null) {
            sendMessage("You are not in a session, type /start to play", ctx.chatId());
          } else {
            session.onMessageReceived(ctx.update().getMessage().getText());
          }
        })
        .build();
  }

  /** A user starts his session with a /start command. We create a new GameSession for them.
   * (userId, GameSession) will be stored in a hashmap userSessions.
   * Then we execute GameSession::onSessionStart.*/
  public Ability receiveStartCommand() {
    return Ability.builder()
        .name("start")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long userId = ctx.user().getId();
          Long chatId = ctx.chatId();
          String username = ctx.user().getFirstName();

          GameSession existingSession = userSessions.get(userId);

          if (existingSession != null) {
            sendMessage("You are already in a session.", chatId);
          } else {
            sendMessage("Session started. Try /duel to try duelling mode or enter a password.", chatId);
            GameSession session = new GameSession(this, chatId, userId, username, database);
            userSessions.put(userId, session);
            session.onSessionStart();
          }
        })
        .build();
  }
  /** Upon receiving /end command, we end the user session.*/
  public Ability receiveEndCommand() {
    return Ability.builder()
        .name("end")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long userId = ctx.user().getId();
          Long chatId = ctx.chatId();

          endGame(userId, chatId);
        })
        .build();
  }
  /** Upon receiving /leaderboard command, we use DatabaseHandler::getFastestFinishers
   * to provide a leaderboard to send to the user.*/
  public Ability receiveLeaderboardCommand() {
    return Ability.builder()
        .name("leaderboard")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long chatId = ctx.chatId();
          List<String> leaderboard = database.getFastestFinishers();

          sendMessage("Top 5 players leaderboard:\n" + String.join("\n", leaderboard), chatId);

        })
        .build();
  }
  public Ability receiveDuelLeaderboardCommand() {
    return Ability.builder()
        .name("duel_leaderboard")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long chatId = ctx.chatId();
          List<String> leaderboard = database.getBestDuelists();

          sendMessage("Top 5 duelists leaderboard:\n" + String.join("\n", leaderboard), chatId);

        })
        .build();
  }
  public Ability receiveDuelCommand() {
    return Ability.builder()
        .name("duel")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long userId = ctx.user().getId();
          Long chatId = ctx.chatId();

          GameSession session = userSessions.get(userId);
          if (session != null && !session.isInDuelMode()) {
            session.enableDuelMode();
            sendMessage("Duel initiated! Looking for an opponent...", chatId);

            initiateDuel(session);
          } else {
            sendMessage("You are already in a duel mode or not in a session.", chatId);
          }
        })
        .build();
  }

  public Ability receiveDuelAcceptCommand() {
    return Ability.builder()
        .name("accept")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long userId = ctx.user().getId();
          Long chatId = ctx.chatId();

          GameSession session = userSessions.get(userId);

          sendMessage("Duel accepted!", chatId);

          session.enableDuelMode();
        })
        .build();
  }
  public Ability receiveDuelDeclineCommand() {
    return Ability.builder()
        .name("decline")
        .locality(ALL)
        .privacy(PUBLIC)
        .action(ctx -> {
          Long userId = ctx.user().getId();
          Long chatId = ctx.chatId();

          sendMessage("Duel declined!", chatId);


        })
        .build();
  }

  private void initiateDuel(GameSession initiatorSession) {
    GameSession opponentSession = findOpponent(initiatorSession);

    if (opponentSession != null && !opponentSession.isInDuelMode()) {
      sendDuelInvitation(initiatorSession, opponentSession);
    } else {
      sendMessage("No available opponents at the moment. Try again later.", initiatorSession.getChatId());
      initiatorSession.disableDuelMode();
      return;
    }

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        if (!opponentSession.isInDuelMode()) {
          // The opponent declined the duel or didn't respond in time
          sendMessage("Duel invitation declined or no response within the time limit.", initiatorSession.getChatId());
          sendMessage("Duel invitation declined or no response within the time limit.", opponentSession.getChatId());
          initiatorSession.disableDuelMode();
          opponentSession.disableDuelMode();
        } else {
          sendMessage("Duel started!", initiatorSession.getChatId());
          sendMessage("Duel started!", opponentSession.getChatId());
          initiatorSession.setDuelOpponent(opponentSession);
          opponentSession.setDuelOpponent(initiatorSession);
          startDuel(initiatorSession, opponentSession);
        }
      }
    }, 10000);
  }


  private GameSession findOpponent(GameSession initiatorSession) {
    for (GameSession session : userSessions.values()) {
      if (session != initiatorSession && !session.isInDuelMode()) {
        return session;
      }
    }
    return null;
  }

  private void sendDuelInvitation(GameSession initiatorSession, GameSession opponentSession) {
    sendMessage("You've been invited to a duel by " + initiatorSession.getUsername() + "!", opponentSession.getChatId());
    sendMessage("Type /accept to accept the duel or /decline to decline within 10 seconds.", opponentSession.getChatId());
  }

  private void startDuel(GameSession player1, GameSession player2) {
    player1.resetSession();
    player2.resetSession();

    player1.onSessionStart();
    player2.onSessionStart();
  }


  /** Sending a message to the user.*/
  public void sendMessage(String text, Long chatId) {
    SendMessage msg = new SendMessage();
    msg.setText(text);
    msg.setChatId(chatId);
    try {
      execute(msg);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
  /** endGame is used to remove user session from the hashmap userSessions.*/
  public void endGame(Long userId, Long chatId) {
    GameSession session = userSessions.remove(userId);
    if (session != null) {
      sendMessage("Game session ended. Type /start to play again.", chatId);
    } else {
      sendMessage("You are not in a session.", chatId);
    }
  }

  public DatabaseHandler getDatabase() {
    return database;
  }
}