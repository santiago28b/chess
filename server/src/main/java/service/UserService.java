package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import exception.*;

import java.util.ArrayList;

public class UserService {

  private UserDao userDao;
  private  AuthDao authDao;
  private GameDao gameDao;

  public UserService(UserDao userDao, AuthDao authDao){
    this.userDao = userDao;
    this.authDao = authDao;
  }
  public UserService(GameDao gameDao,UserDao userDao, AuthDao authDao){
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
    gameDao.clear();
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

  public void joinGameService(String token, int gameID, String playerColor) {
    try{
      if(gameID == 0 || token == null || playerColor == null){
        throw  new BadRequestException("Error: bad request");
      }
      if(!authDao.validateToken(token)){
        throw new UnauthorizedException("Error: unauthorized");
      }
      if(playerColor.equals("WHITE") && gameDao.getGame(gameID).whiteUsername() != null){
        throw new AlreadyTakenException("Error: already taken");
      }
      if(playerColor.equals("BLACK") && gameDao.getGame(gameID).blackUsername() != null){
        throw new AlreadyTakenException("Error: already taken");
      }
      GameData game = gameDao.getGame(gameID);
      if(playerColor.equals("WHITE")){
        gameDao.updateGame(game.gameID(),authDao.getAuth(token).username(),game.blackUsername(),game.gameName(),game.game());
      } else if (playerColor.equals("BLACK")) {
        gameDao.updateGame(game.gameID(),game.whiteUsername(),authDao.getAuth(token).username(),game.gameName(),game.game());
      }
    } catch (DataAccessException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public ArrayList<GameData> getGames(String token){
    try{
      if(authDao.validateToken(token)){
        return gameDao.listGames();
      } else{
        throw new RuntimeException("Error: unauthorized");
      }
    } catch (DataAccessException e){
      throw new RuntimeException(e.getMessage());
    }
  }
}
