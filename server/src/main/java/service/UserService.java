package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDao;
import dataaccess.MemoryUserDao;
import model.AuthData;
import model.UserData;

public class UserService {

  private MemoryUserDao userDao;
  private MemoryAuthDao authDao;

  public UserService(MemoryUserDao userDao, MemoryAuthDao authDao){
    this.userDao = userDao;
    this.authDao = authDao;
  }

  public AuthData register(UserData user) {

    try{
      userDao.createUser(user);
      String token = authDao.createAuth(user);
      return new AuthData(token, user.userName());
    } catch (DataAccessException e){
      throw  new RuntimeException(e.getMessage());
    }
  }
}
