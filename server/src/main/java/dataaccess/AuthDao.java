package dataaccess;


import model.AuthData;
import model.UserData;

public interface AuthDao {
  void clear() throws DataAccessException;
  String createAuth(UserData user) throws DataAccessException;

  public AuthData getAuth(String username);

  void deleteAuth(String authToken) throws DataAccessException;


  boolean validateToken(String token) throws DataAccessException;
}
