package dataaccess;


import model.AuthData;
import model.UserData;

public interface AuthDao {
  void clear();
  String createAuth(UserData user);

  public AuthData getAuth(String token);

  void deleteAuth(String authToken) throws DataAccessException;


}
