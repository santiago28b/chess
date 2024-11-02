package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class SQLUserDao extends AbstractSQLDAO implements UserDao{

  public SQLUserDao() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public void clear() throws DataAccessException {
    var statement = "TRUNCATE TABLE user";
    executeUpdate(statement);
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
    alreadyExist(user);
    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
    executeUpdate(statement, user.username(), hashedPassword, user.email());
  }

  private static void alreadyExist(UserData user) throws DataAccessException {
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
}
