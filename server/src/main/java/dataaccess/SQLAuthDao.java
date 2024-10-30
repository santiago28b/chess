package dataaccess;

import model.AuthData;
import model.UserData;

public class SQLAuthDao implements AuthDao {
  @Override
  public void clear() {

  }

  @Override
  public String createAuth(UserData user) {
    return "";
  }

  @Override
  public AuthData getAuth(String username) {
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

  }
}
