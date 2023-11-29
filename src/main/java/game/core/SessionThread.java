package game.core;

public class SessionThread extends Thread {
    private GameSession currentSession;
    public SessionThread(GameSession newSession) {
        super(newSession);
        currentSession = newSession;
    }
    public void putMessage(String message) {
        currentSession.putMessage(message);
    }
}
