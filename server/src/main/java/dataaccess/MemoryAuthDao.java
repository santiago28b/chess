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
    authListMemory.clear();
  }

  public String createAuth(UserData user) {
    UUID randomCode = UUID.randomUUID();
    String token = randomCode.toString();
    AuthData authData = new AuthData(token,user.username());
    authListMemory.put(token,authData);
    return token;
  }

  @Override
  public AuthData getAuth(String username){
    return authListMemory.get(username);
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    AuthData storedToken = authListMemory.get(authToken);
    if(storedToken != null){
      authListMemory.remove(authToken);
    } else{
      throw new DataAccessException("Error: unauthorized");
    }
  }
}
