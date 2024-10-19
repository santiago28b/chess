package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDao;
import dataaccess.MemoryGameDao;
import dataaccess.MemoryUserDao;
import model.AuthData;
import model.GameData;
import model.UserData;

public class UserService {

  private  MemoryUserDao userDao;
  private  MemoryAuthDao authDao;
  private MemoryGameDao gameDao;

  public UserService(MemoryUserDao userDao, MemoryAuthDao authDao){
    this.userDao = userDao;
    this.authDao = authDao;
  }
  public UserService(MemoryGameDao gameDao,MemoryUserDao userDao, MemoryAuthDao authDao){
    this.gameDao = gameDao;
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
  public void logoutUser(String token) {
    try {
      authDao.deleteAuth(token);
    } catch (DataAccessException e){
      throw new RuntimeException(e.getMessage());
    }
  }


  public void clearData() {
    userDao.clear();
    authDao.clear();
  }

  public int createGame(String token,String gameName){
    if(token == null || gameName == null){
      throw  new IllegalArgumentException("Error: bad request");
    }
    try{
      if(authDao.validateToken(token)){
        return gameDao.createGame(gameName);
      } else{
        throw new RuntimeException("Error: unauthorized");
      }
    } catch (DataAccessException e){
      throw new RuntimeException(e.getMessage());
    }
  }
}
