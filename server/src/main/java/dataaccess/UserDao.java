package dataaccess;

import model.UserData;

public interface UserDao {

  void clear();
  UserData getData(UserData user)throws DataAccessException;

  void createUser(UserData user)throws DataAccessException;
}
