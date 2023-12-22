import game.core.GameSession;
import game.core.DatabaseHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
public class GameSessionDuelTest {
  private DatabaseHandler database;
  @Before
  public void setUp() {
    database = new DatabaseHandler("jdbc:sqlite:src/main/java/game/database/test_game_sessions.db");
  }
  @After
  public void tearDown() {
    Connection connection = database.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "DELETE FROM game_sessions"
    )) {
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Test
  public void testDuelModeWin() throws InterruptedException {

    GameSession player1 = new GameSession(null, 1L, 1L, "player1", database);
    player1.enableDuelMode();

    GameSession player2 = new GameSession(null, 2L, 2L, "player2", database);
    player2.enableDuelMode();
    player1.setDuelOpponent(player2);
    player2.setDuelOpponent(player1);

    player1.onSessionStart();
    player2.onSessionStart();

    String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    String currentDatePalindrome = new StringBuilder(currentDate).reverse().toString();
    player1.onMessageReceived("\uD83D\uDE0Ered0E#" + currentDate + currentDatePalindrome + "#E0der\uD83D\uDE0E");
    Thread.sleep(1000);

    assertFalse(player1.isInDuelMode());
    assertTrue(player2.isInDuelMode());

    player2.onMessageReceived("\uD83D\uDE0Ered0E#" + currentDate + currentDatePalindrome + "#E0der\uD83D\uDE0E");

    assertFalse(player2.isInDuelMode());

    int wonDuelsPlayer1 = getWonDuelsFromDatabase(1L);
    int wonDuelsPlayer2 = getWonDuelsFromDatabase(2L);
    assertEquals(1, wonDuelsPlayer1);
    assertEquals(0, wonDuelsPlayer2);
  }

  private int getWonDuelsFromDatabase(Long userId) {
    Connection connection = database.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT won_duels FROM game_sessions WHERE user_id = ?"
    )) {
      preparedStatement.setLong(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        return resultSet.getInt("won_duels");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0;
  }
}