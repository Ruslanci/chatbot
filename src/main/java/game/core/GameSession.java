package game.core;

import game.logic.PasswordProcessor;
import game.telegram.ChatBot;
import java.sql.Timestamp;
import java.util.LinkedList;

// Represents a user's game session.
public class GameSession implements GameSessionInterface {

  private PasswordProcessor processor;
  private final Long chatId;
  private final Long userId;
  private final String username;
  private final DatabaseHandler database;
  private final ChatBot chatBot;
  private Timestamp startTime;
  private boolean duelMode;
  private boolean sessionStarted;
  private GameSession duelOpponent;

  public GameSession(ChatBot chatBot, Long chatId, Long userId, String username, DatabaseHandler database) {
    this.processor = new PasswordProcessor();
    this.chatId = chatId;
    this.userId = userId;
    this.username = username;
    this.chatBot = chatBot;
    this.database = database;
    this.duelMode = false;
    this.sessionStarted = false;
    this.duelOpponent = null;
  }
  public void onSessionStart() {
    sessionStarted = true;
    this.startTime = new Timestamp(System.currentTimeMillis());
  }
   /**Receives a new password from the user, checking it using checkPassword.
   and then send the user the result of the examination.*/
  public void onMessageReceived(String message) {
    if (chatBot == null) {
      this.checkPassword(message);
    } else if (message != null) {
      this.checkPassword(message).forEach(msg -> chatBot.sendMessage(msg, chatId));
    }
    // processor.isFinished() returns whether or not the user satisfied all password conditions.
    if (processor.isFinished()) {
      onSessionEnd();
    }

  }
  /** When the user completed the game, we need to store the duration it took him to finish.
   A DatabaseHandler::saveGameSessionToDatabase is used.*/
  public void onSessionEnd() {
    long duration = getDuration();

    database.saveGameSessionToDatabase(userId, username, duration);
    if (chatBot != null) {

      if (isInDuelMode()) {
        if (!duelOpponent.isFinished()) {
          chatBot.sendMessage("Congratulations, you've won the duel! Your duration: " + duration, chatId);
          disableDuelMode();
          duelOpponent.disableDuelMode();
        } else {
          chatBot.sendMessage("You've lost the duel. Your duration: " + duration, chatId);
        }
      } else {
        chatBot.sendMessage(
            "Congratulations, " + username + "! Type /leaderboard to see who's the best!", chatId);
        chatBot.endGame(userId, chatId);
      }

    }
  }

  public void resetSession() {
    this.processor = new PasswordProcessor();
  }

  public LinkedList<String> checkPassword(String password) {
    return processor.processAndGetStatus(password);
  }
  public long getDuration() {
    if (startTime == null) {
      return 0;
    }
    Timestamp endTime = new Timestamp(System.currentTimeMillis());
    return endTime.getTime() - startTime.getTime();
  }
  public GameSession declareWinner(GameSession opponent) {
    long duration1 = this.getDuration();
    long duration2 = opponent.getDuration();

    if (duration1 < duration2) {
      return this;
    } else if (duration2 < duration1) {
      return opponent;
    } else {
      return null;
    }
  }
  public void setDuelOpponent(GameSession opponent) {
    this.duelOpponent = opponent;
  }
  public void enableDuelMode() {
    this.duelMode = true;
  }

  public void disableDuelMode() {
    this.duelMode = false;
  }

  public boolean isInDuelMode() {
    return duelMode;
  }

  public Long getChatId() {
    return chatId;
  }
  public Long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }
  public boolean isSessionStarted() {
    return sessionStarted;
  }

  public boolean isFinished() {
    return processor.isFinished();
  }



}
