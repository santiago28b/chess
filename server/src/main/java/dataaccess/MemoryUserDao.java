package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDao implements UserDao {

  private Map<String,UserData> userListMemory = new HashMap<>();
  public void createUser(UserData user) {

  }
}
