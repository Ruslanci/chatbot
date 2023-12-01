package game.telegram;

import static org.telegram.abilitybots.api.objects.Flag.TEXT;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import game.core.DatabaseHandler;
import game.core.GameSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


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
}