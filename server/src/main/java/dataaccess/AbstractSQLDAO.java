package dataaccess;

import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AbstractSQLDAO {


  public int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();
        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }
        return 0;
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
          """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """,
          """
            CREATE TABLE IF NOT EXISTS game(
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
  };

  void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage())); //I had a 500 here before
    }
  }
}
