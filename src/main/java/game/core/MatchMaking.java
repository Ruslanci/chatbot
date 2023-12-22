package game.core;

import game.telegram.ChatBot;
import java.util.LinkedList;
import java.util.Queue;

public class MatchMaking {
    private ChatBot chatBot;
    private Queue<Long> queue;
    public static final long TIMEOUT = 10000L;
    public MatchMaking() {
        queue = new LinkedList<Long>();
    }
}
