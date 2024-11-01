package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDao implements UserDao{

  public SQLUserDao() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public void clear() {

  }

  @Override
  public UserData getData(UserData user) throws DataAccessException {
    return null;
  }

  @Override
  public void createUser(UserData user) throws DataAccessException {
    if (user.username() == null || user.password() == null || user.email() == null) {
      throw new DataAccessException("Error: bad request - missing required fields");
    }
    extracted(user);
    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
    executeUpdate(statement, user.username(), hashedPassword, user.email());
  }

  private static void extracted(UserData user) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var checkStatement = "SELECT 1 FROM user WHERE username = ?";
      try (var ps = conn.prepareStatement(checkStatement)) {
        ps.setString(1, user.username());
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            throw new DataAccessException("Error: username already taken");
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Different error  " + e.getMessage());
    }
  }


  private int executeUpdate(String statement, Object... params) throws DataAccessException {
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
            """
  };

  private void configureDatabase() throws DataAccessException {
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
