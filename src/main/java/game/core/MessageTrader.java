package game.core;

public class MessageTrader {
    private String message;
    private boolean valueSet = false;
    synchronized String get() {
        while (!valueSet) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        valueSet = false;
        notify();
        return message;
    }
    synchronized void put(String message) {
        while(valueSet) {
            try {
                wait();
            }
            catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        this.message = message;
        valueSet = true;
        notify();
    }
}
