package game.core;
import game.logic.PasswordProcessor;
import game.telegram.ChatBot;

import java.util.LinkedList;
import java.util.Scanner;

public class Session implements Runnable {
    private final PasswordProcessor processor;
    private final MessageTrader channel;
    private final Long chatId;
    private final ChatBot holder;
    public Session(ChatBot currentHolder, Long currentChatId) {
        processor = new PasswordProcessor();
        channel = new MessageTrader();
        chatId = currentChatId;
        holder = currentHolder;
    }
    public void putMessage(String message) {
        channel.put(message);
    }
    public LinkedList<String> checkPassword(String password) {
        return processor.processAndGetStatus(password);
    }

    @Override
    public void run() {
        while(!processor.isFinished()) {
            holder.sendText("Enter your password:", chatId);
            this.checkPassword(channel.get()).forEach(msg -> holder.sendText(msg, chatId));
        }
        holder.sendText("Congratulation!", chatId);
    }
}
