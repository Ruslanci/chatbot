package game.core;
import game.logic.PasswordProcessor;
import game.telegram.ChatBot;
import game.util.MessageTrader;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class GameSession {
    private final PasswordProcessor processor;
    private final MessageTrader channel;
    private final Long chatId;
    private final ChatBot chatBot;
    private boolean running;
    public GameSession(ChatBot chatBot, Long chatId) {
        this.processor = new PasswordProcessor();
        this.channel = new MessageTrader();
        this.chatId = chatId;
        this.chatBot = chatBot;
        this.running = false;
    }
    public void start() {
        running = true;
        runSession();
    }
    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
    public void putMessage(String message) {
        channel.put(message);
    }
    public LinkedList<String> checkPassword(String password) {
        return processor.processAndGetStatus(password);
    }

    public void runSession() {
        CompletableFuture.runAsync(() -> {
            while (!processor.isFinished() && running) {
                chatBot.sendText("Enter your password:", chatId);
                String userEnteredPassword = channel.get();

                if (userEnteredPassword != null) {
                    this.checkPassword(userEnteredPassword).forEach(msg -> chatBot.sendText(msg, chatId));
                }
            }

            if (running) {
                chatBot.sendText("Congratulations! Type /start to begin a new game.", chatId);
                running = false;
            }
        });
    }
}
