package game.core.sessions;

import game.logic.PasswordProcessor;
import game.telegram.ChatBot;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Objects;

public class DuelSession extends GameSession {
    private final Long firstUserId;
    private final Long secondUserId;
    private final Long firstChatId;
    private final Long secondChatId;
    private final String firstUsername;
    private final String secondUsername;
    private final PasswordProcessor firstProcessor;
    private final PasswordProcessor secondProcessor;
    private final ChatBot chatBot;
    private Timestamp startTime;
    public DuelSession(ChatBot chatBot,
                       Long firstChatId, Long secondChatId,
                       Long firstUserId, Long secondUserId,
                       String firstUsername, String secondUsername) {
        this.chatBot = chatBot;
        this.firstChatId = firstChatId;
        this.secondChatId = secondChatId;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.firstUsername = firstUsername;
        this.secondUsername = secondUsername;
        firstProcessor = new PasswordProcessor();
        secondProcessor = new PasswordProcessor();
    }

    @Override
    public void onSessionStart() {
        this.startTime = new Timestamp(System.currentTimeMillis());
        if (chatBot != null) {
            chatBot.sendMessage("Enter a password: ", firstChatId);
            chatBot.sendMessage("Enter a password: ", secondChatId);
        }
    }

    @Override
    public void onMessageReceived(String message, Long sourceId) {
        if (Objects.equals(sourceId, firstChatId))
            firstProcessor.processAndGetStatus(message).forEach(msg -> chatBot.sendMessage(msg, sourceId));
        else if (Objects.equals(sourceId, secondChatId))
            secondProcessor.processAndGetStatus(message).forEach(msg -> chatBot.sendMessage(msg, sourceId));
        if (firstProcessor.isFinished() || secondProcessor.isFinished())
            onSessionEnd();
    }
    private void onSessionEnd() {
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        long duration = endTime.getTime() - startTime.getTime();
        String winner = null;
        if (firstProcessor.isFinished()) winner = firstUsername;
        else if (secondProcessor.isFinished()) winner = secondUsername;
        if (chatBot != null && winner != null) {
            String message = "Player " + winner + " won with time " + duration;
            chatBot.sendMessage(message, firstChatId);
            chatBot.sendMessage(message, secondChatId);
            chatBot.endGame(firstUserId, firstChatId);
            chatBot.endGame(secondUserId, secondChatId);
        }
    }
}
