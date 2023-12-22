package game.core.sessions;

public class SessionMetaData {
    private final boolean correctlyFinished;
    private final boolean isDuel;
    private final long duration;
    private final String username;
    public SessionMetaData(boolean correctlyFinished, boolean isDuel, long duration, String username) {
        this.isDuel = isDuel;
        this.correctlyFinished = correctlyFinished;
        this.duration = duration;
        this.username = username;
    }
    public boolean isDuel() {
        return isDuel;
    }
    public long duration() {
        return duration;
    }
    public String username() {
        return username;
    }
    public boolean isCorrectlyFinished() {
        return correctlyFinished;
    }
}
