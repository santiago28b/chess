package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDao;
import dataaccess.MemoryUserDao;
import model.AuthData;
import model.UserData;

public class UserService {

  private  MemoryUserDao userDao;
  private  MemoryAuthDao authDao;

  public UserService(MemoryUserDao userDao, MemoryAuthDao authDao){
    this.userDao = userDao;
    this.authDao = authDao;
  }

  public UserService() {

  }

  public AuthData register(UserData user){

    try{
      userDao.createUser(user);
      String token = authDao.createAuth(user);
      return new AuthData(token, user.username());
    } catch (DataAccessException e){
      throw  new RuntimeException(e.getMessage());
    }
  }

  public AuthData login(UserData loginUser) {
    try {
      userDao.getData(loginUser);
      String token = authDao.createAuth(loginUser);
      return new AuthData(token, loginUser.username());
    } catch (DataAccessException e) {
      throw new RuntimeException(e.getMessage());
    }
  }


}
