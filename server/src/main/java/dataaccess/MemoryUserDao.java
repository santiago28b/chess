package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDao implements UserDao {

  private final Map<String,UserData> userListMemory = new HashMap<>();

  @Override
  public void clear() {
    userListMemory.clear();

  }

  @Override
  public UserData getData(UserData user) throws DataAccessException {
    if(!userListMemory.containsKey(user.username())){
      throw  new DataAccessException("Error: unauthorized");
    }
    UserData storedUser = userListMemory.get(user.username());
    if(!storedUser.password().equals(user.password())){
      throw  new DataAccessException("Error: unauthorized");
    }
    return storedUser;
  }
  @Override
  public void createUser(UserData user) throws DataAccessException {
    if(userListMemory.containsValue(user)){
      throw  new DataAccessException("Error: already taken");
    } else if (user.username()== null || user.password() == null || user.email()==null) {
      throw new DataAccessException("Error: bad request");
    }
    userListMemory.put(user.username(),user);

  }

}



