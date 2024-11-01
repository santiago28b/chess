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
