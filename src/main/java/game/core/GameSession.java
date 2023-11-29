package game.core;
import game.logic.PasswordProcessor;
import game.telegram.ChatBot;
import game.util.MessageTrader;

import java.util.LinkedList;

public class GameSession implements Runnable {
    private final PasswordProcessor processor;
    private final MessageTrader channel;
    private final Long chatId;
    private final ChatBot holder;
    public GameSession(ChatBot holder, Long chatId) {
        this.processor = new PasswordProcessor();
        this.channel = new MessageTrader();
        this.chatId = chatId;
        this.holder = holder;
    }
    public void putMessage(String message) {
        channel.put(message);
    }
    public LinkedList<String> checkPassword(String password) {
        return processor.processAndGetStatus(password);
    }

    @Override
    public void run() {
        while(!processor.isFinished() && !Thread.currentThread().isInterrupted()) {
            holder.sendText("Enter your password:", chatId);
            this.checkPassword(channel.get()).forEach(msg -> holder.sendText(msg, chatId));
        }
        if (!Thread.currentThread().isInterrupted()) {
            holder.sendText("Congratulation!", chatId);
        }
    }
}
