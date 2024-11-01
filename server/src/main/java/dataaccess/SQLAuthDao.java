package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDao extends AbstractSQLDAO implements AuthDao {
  @Override
  public void clear() {

  }

  @Override
  public String createAuth(UserData user) throws DataAccessException {
    String token = generateToken();
    var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
    executeUpdate(statement, user.username(),token);
    return token;
  }

  @Override
  public AuthData getAuth(String username) {

    return null;
  }


  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    // Use a single try-catch to control the flow better
    try (var conn = DatabaseManager.getConnection()) {
      // Step 1: Check if the token exists
      var checkStatement = "SELECT 1 FROM auth WHERE authToken = ?";
      try (var ps = conn.prepareStatement(checkStatement)) {
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (!rs.next()) { // If no result, token is not in the table
            throw new DataAccessException("Error: unauthorized");
          }
        }
      }
      // Step 2: Delete the token if it exists
      var deleteStatement = "DELETE FROM auth WHERE authToken = ?";
      executeUpdate(deleteStatement, authToken);

    } catch (SQLException e) {
      throw new DataAccessException("Database error during token deletion: " + e.getMessage());
    }
  }


  private String generateToken() {
    // Implement token generation logic here (UUID, JWT, etc.)
    return UUID.randomUUID().toString();
  }

  @Override
  public boolean validateToken(String token) {
    return false;
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL,
              `token` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
  };


}
