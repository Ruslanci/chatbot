package game.core.sessions;

import game.core.DatabaseHandler;
import game.logic.PasswordProcessor;
import game.telegram.ChatBot;
import java.sql.Timestamp;
import java.util.LinkedList;
public abstract class GameSession {
    public abstract void onSessionStart();
    public abstract void onMessageReceived(String message, Long sourceId);

}
