package dataaccess;

import model.UserData;

public interface AuthDao {
  void clear();

  UserData getData(UserData user)throws DataAccessException;

  void CreateUser(UserData user)throws DataAccessException;
}
