package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public class SQLAuthDao implements AuthDao {
  @Override
  public void clear() {

  }

  @Override
  public String createAuth(UserData user) {
    return UUID.randomUUID().toString();
  }

  @Override
  public AuthData getAuth(String username) {
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

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
