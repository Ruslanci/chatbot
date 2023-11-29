package game.util;

public class MessageTrader {
    private String message;
    private boolean hasMessage = false;
    public synchronized String get() {
        while (!hasMessage) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        hasMessage = false;
        notify();
        return message;
    }
    public synchronized void put(String message) {
        while(hasMessage) {
            try {
                wait();
            }
            catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        this.message = message;
        hasMessage = true;
        notify();
    }
}
