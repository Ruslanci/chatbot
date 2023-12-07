import game.core.DatabaseHandler;
import game.core.GameSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

public class GameSessionDurationTest {
  private DatabaseHandler database;
  @Before
  public void setUp() {
    database = new DatabaseHandler();
  }
  @After
  public void tearDown() {
    Connection connection = database.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "DELETE FROM game_sessions WHERE user_id = ?"
    )) {
      preparedStatement.setLong(1, 1L);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Test
  public void testSaveGameSessionWithDurationGreaterThanTimeout() throws InterruptedException {
    long timeout = 10000;

    GameSession session = new GameSession(null, 1L, 1L, "TestUser", database);

    session.onSessionStart();
    session.onMessageReceived("7-12-2023#redder#3202-21-70E");

    Thread.sleep(timeout);

    session.onMessageReceived("\uD83D\uDE0EE07-12-2023#redder#3202-21-70E\uD83D\uDE0E");

    long durationFromDatabase = getDurationFromDatabase(1L);

    assertTrue("Duration should be greater than the timeout", durationFromDatabase > timeout);
  }

  private long getDurationFromDatabase(Long userId) {
    Connection connection = database.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT duration FROM game_sessions WHERE user_id = ?"
    )) {
      preparedStatement.setLong(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        return resultSet.getLong("duration");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0L;
  }
}
