package game.core;
import game.logic.PasswordProcessor;
import game.telegram.ChatBot;

import java.util.LinkedList;

public class GameSession {
    private final PasswordProcessor processor;
    private final Long chatId;
    private final Long userId;

    private final ChatBot chatBot;
    public GameSession(ChatBot chatBot, Long chatId, Long userId) {
        this.processor = new PasswordProcessor();
        this.chatId = chatId;
        this.userId = userId;
        this.chatBot = chatBot;
    }
    public void start() {
        chatBot.sendText("Enter a password: ", chatId);
    }

    public void putMessage(String message) {
        String userEnteredPassword = message;

        if (userEnteredPassword != null) {
            this.checkPassword(userEnteredPassword).forEach(msg -> chatBot.sendText(msg, chatId));
        }
        if (processor.isFinished()) {
            chatBot.sendText("Congratulations!", chatId);
            chatBot.endGame(userId, chatId);
        }

    }
    public LinkedList<String> checkPassword(String password) {
        return processor.processAndGetStatus(password);
    }

}
