package dataaccess;

import model.UserData;

public interface UserDao {

  void clear() throws DataAccessException;
  UserData getData(UserData user)throws DataAccessException;

  void createUser(UserData user)throws DataAccessException;
}
