package game.core;

import game.logic.PasswordProcessor;
import game.telegram.ChatBot;
import java.sql.Timestamp;
import java.util.LinkedList;

// Represents a user's game session.
public class GameSession implements GameSessionInterface {

  private final PasswordProcessor processor;
  private final Long chatId;
  private final Long userId;
  private final String username;
  private final DatabaseHandler database;
  private final ChatBot chatBot;
  private Timestamp startTime;

  public GameSession(ChatBot chatBot, Long chatId, Long userId, String username, DatabaseHandler database) {
    this.processor = new PasswordProcessor();
    this.chatId = chatId;
    this.userId = userId;
    this.username = username;
    this.chatBot = chatBot;
    this.database = database;
  }
  public void onSessionStart() {
    this.startTime = new Timestamp(System.currentTimeMillis());
    chatBot.sendMessage("Enter a password: ", chatId);
  }
   /**Receives a new password from the user, checking it using checkPassword.
   and then send the user the result of the examination.*/
  public void onMessageReceived(String message) {

    if (message != null) {
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
    Timestamp endTime = new Timestamp(System.currentTimeMillis());
    long duration = endTime.getTime() - startTime.getTime();
    database.saveGameSessionToDatabase(userId, username, duration);
    chatBot.sendMessage("Congratulations, " + username + "! Type /leaderboard to see who's the best!", chatId);
    chatBot.endGame(userId, chatId);
  }

  public LinkedList<String> checkPassword(String password) {
    return processor.processAndGetStatus(password);
  }




}
