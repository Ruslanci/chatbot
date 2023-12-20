package game.core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Handles the database operations for the Password Game.
public class DatabaseHandler {
  private final Connection connection;
  public DatabaseHandler() {
    connection = initializeDatabase();
  }

  /** Initializes the SQLite database and creates a table game_sessions if it doesn't exist.
   * @return the database connection.  */
  private Connection initializeDatabase() {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection connection = DriverManager.getConnection(
          "jdbc:sqlite:src/main/java/game/database/game_sessions.db");
      try (Statement statement = connection.createStatement()) {
        statement.execute(
            "CREATE TABLE IF NOT EXISTS game_sessions "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "username TEXT NOT NULL, "
                + "duration INTEGER NOT NULL,"
                + "won_duels INTEGER NOT NULL DEFAULT 0)"
        );
      }

      return connection;
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException("Error initializing database", e);
    }
  }

  /** Retrieves the fastest finishers from the 'game_sessions' table.
   * @return A list of strings representing the leaderboard entries. */
  public List<String> getFastestFinishers() {
    List<String> leaderboard = new ArrayList<>();

    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT user_id, username, MIN(duration) AS fastest_duration, won_duels " +
            "FROM game_sessions " +
            "WHERE duration IS NOT NULL " +
            "GROUP BY user_id, username, won_duels " +
            "ORDER BY fastest_duration ASC " +
            "LIMIT 5"
    )) {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String username = resultSet.getString("username");
        long fastestDuration = resultSet.getLong("fastest_duration");
        int wonDuels = resultSet.getInt("won_duels");
        leaderboard.add(
            "Username: " + username + ", Fastest Duration: " + fastestDuration / 1000.0f + " seconds"
                + ", Won Duels: " + wonDuels);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return leaderboard;
  }
  public List<String> getBestDuelists() {
    List<String> bestDuelists = new ArrayList<>();

    try (PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT user_id, username, won_duels " +
            "FROM game_sessions " +
            "WHERE won_duels > 0 " +
            "ORDER BY won_duels DESC " +
            "LIMIT 5"
    )) {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String username = resultSet.getString("username");
        int wonDuels = resultSet.getInt("won_duels");
        bestDuelists.add("Username: " + username + ", Won Duels: " + wonDuels);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return bestDuelists;
  }
  /** Saves a game session to the 'game_sessions' table.
   * @param userId   The user ID associated with the session.
   * @param username The username associated with the session.
   * @param duration The duration of the session. */
  public void saveGameSessionToDatabase(Long userId, String username, long duration, boolean wonDuel) {
    try {
      // Check if the user already exists in the database
      try (PreparedStatement checkStatement = connection.prepareStatement(
          "SELECT duration, won_duels FROM game_sessions WHERE user_id = ?"
      )) {
        checkStatement.setLong(1, userId);
        ResultSet resultSet = checkStatement.executeQuery();

        if (resultSet.next()) {
          // User exists, check if the new duration is smaller
          long existingDuration = resultSet.getLong("duration");
          int existingWonDuels = resultSet.getInt("won_duels");
          if (duration < existingDuration) {
            // Update the existing entry with the new duration
            try (PreparedStatement updateStatement = connection.prepareStatement(
                "UPDATE game_sessions SET duration = ?, won_duels = ? WHERE user_id = ?"
            )) {
              updateStatement.setLong(1, duration);
              updateStatement.setLong(2, wonDuel ? existingWonDuels + 1 : existingWonDuels);
              updateStatement.setLong(3, userId);
              updateStatement.executeUpdate();
            }
          }
        } else {
          // User does not exist, insert a new entry
          try (PreparedStatement insertStatement = connection.prepareStatement(
              "INSERT INTO game_sessions (user_id, username, duration, won_duels) VALUES (?, ?, ?, 0)"
          )) {
            insertStatement.setLong(1, userId);
            insertStatement.setString(2, username);
            insertStatement.setLong(3, duration);
            insertStatement.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
