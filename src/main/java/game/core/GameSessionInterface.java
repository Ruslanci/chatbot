package game.core;

public interface GameSessionInterface {

  public void onMessageReceived(String message);

  public void onSessionStart();

  public void onSessionEnd();
}
