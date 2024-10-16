package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDao implements UserDao {

  private final Map<String,UserData> userListMemory = new HashMap<>();

  @Override
  public void clear() {

  }

  @Override
  public UserData getData(UserData user) throws DataAccessException {
    return null;
  }

  @Override
  public void createUser(UserData user) throws DataAccessException {
    if(userListMemory.containsValue(user)){
      throw  new DataAccessException("Error: already taken");
    } else if (user.userName()== null || user.password() == null || user.email()==null) {
      throw new DataAccessException("Error: bad request");
    }
    userListMemory.put(user.userName(),user);

  }
}
