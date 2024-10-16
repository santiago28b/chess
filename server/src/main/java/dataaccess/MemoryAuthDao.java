package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDao  implements AuthDao{

  private final Map<String, AuthData> authListMemory = new HashMap<>();

  @Override
  public void clear() {

  }

  public String createAuth(UserData user) {
    UUID randomCode = UUID.randomUUID();
    String token = randomCode.toString();
    AuthData authData = new AuthData(token,user.userName());
    authListMemory.put(token,authData);
    return token;
  }

  @Override
  public AuthData getAuth(String token) {
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

  }
}
