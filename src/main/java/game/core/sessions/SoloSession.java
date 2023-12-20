package game.core.sessions;

import game.core.DatabaseHandler;
import game.logic.PasswordProcessor;
import game.telegram.ChatBot;

import java.sql.Timestamp;
import java.util.LinkedList;

public class SoloSession extends GameSession {
    private final PasswordProcessor processor;
    private final Long chatId;
    private final Long userId;
    private final String username;
    private final DatabaseHandler database;
    private final ChatBot chatBot;
    private Timestamp startTime;

    public SoloSession(ChatBot chatBot, Long chatId, Long userId, String username, DatabaseHandler database) {
        this.processor = new PasswordProcessor();
        this.chatId = chatId;
        this.userId = userId;
        this.username = username;
        this.chatBot = chatBot;
        this.database = database;
    }
    @Override
    public void onSessionStart() {
        this.startTime = new Timestamp(System.currentTimeMillis());
        if (chatBot != null) {
            chatBot.sendMessage("Enter a password: ", chatId);
        }
    }
    @Override
    public void onMessageReceived(String message, Long sourceId) {
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
    private void onSessionEnd() {
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        long duration = endTime.getTime() - startTime.getTime();
        database.saveGameSessionToDatabase(userId, username, duration);
        if (chatBot != null) {
            chatBot.sendMessage(
                    "Congratulations, " + username + "! Type /leaderboard to see who's the best!", chatId);
            chatBot.endGame(userId, chatId);
        }
    }

    public LinkedList<String> checkPassword(String password) {
        return processor.processAndGetStatus(password);
    }
}
