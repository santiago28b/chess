package service;

import dataaccess.MemoryAuthDao;
import dataaccess.MemoryGameDao;
import dataaccess.MemoryUserDao;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {
  String username = "testUser";
  String password = "password";
  String email = "test@g.com";
  private UserService userService; // Your service class
  private MemoryUserDao userDao; // Your DAO
  private MemoryAuthDao authDao; // Your Auth DAO
  private MemoryGameDao gameDao;

  @BeforeEach
  public void setUp() {
    userDao = new MemoryUserDao(); // Initialize your DAO
    authDao = new MemoryAuthDao();//
    gameDao = new MemoryGameDao();
    userService = new UserService(gameDao, userDao,authDao); // Initialize your service
  }

  @Test
  void validRegister() {

    UserData testUser = new UserData(username,password,email);

     userService.register(testUser);
    Assertions.assertEquals(username,userDao.getUsername(testUser));
  }
  @Test
  void failToRegisterDuplicateUsername() {
    UserData testUser=new UserData(username, password, email);
    userService.register(testUser);
    UserData duplicateUser=new UserData(username, password, email);

    assertThrows(RuntimeException.class, () -> userService.register(duplicateUser));
  }

  @Test
  void validLogin(){
    UserData testUser=new UserData(username,password,email);
    userService.register(testUser);
    Assertions.assertEquals(username,userDao.getUsername(testUser));
    userService.login(testUser);
    var loginResponse = userService.login(testUser); // Log in the user
    // Then
    assertNotNull(loginResponse.authToken());

  }
  @Test
  void invalidLogin(){
    UserData testUser=new UserData(username,password,email);
    assertThrows(RuntimeException.class, () -> userService.login(testUser));
  }

  @Test
  void validLogout(){
    UserData testUser=new UserData(username,password,email);
    userService.register(testUser);
    var authdata = userService.login(testUser);
   String token = authdata.authToken();
   userService.logoutUser(token);
   assertNull(authDao.getAuth(username));
  }

  @Test
  void invalidLogout(){
    UserData testUser=new UserData(username,password,email);
    assertThrows(RuntimeException.class, () -> userService.logoutUser(password));
  }

@Test
  void validCreate(){
  UserData testUser=new UserData(username,password,email);
  userService.register(testUser);
  var authdata = userService.login(testUser);
  String token = authdata.authToken();
  userService.createGame(token,"testGame");
  Assertions.assertNotNull(gameDao);
}
@Test
  void invalidCreate(){
    UserData testUser=new UserData(username,password,email);
    userService.register(testUser);
    var authdata = userService.login(testUser);
    assertThrows(RuntimeException.class, () -> userService.createGame(password,"testGame"));
}

@Test
  void validJoin(){
    UserData testUser=new UserData(username,password,email);
    userService.register(testUser);
    var authdata = userService.login(testUser);
    String token = authdata.authToken();
    userService.createGame(token,"testGame");
   userService.joinGameService(token,1,"WHITE");
   Assertions.assertEquals(username,gameDao.getGame(1).whiteUsername());
}

@Test
  void invalidJoin(){
  UserData testUser=new UserData(username,password,email);
  userService.register(testUser);
  var authdata = userService.login(testUser);
  String token = authdata.authToken();
  userService.createGame(token,"testGame");
  assertThrows(RuntimeException.class, () -> userService.joinGameService(token,1,null));
}

@Test
  void validList(){
  UserData testUser=new UserData(username,password,email);
  userService.register(testUser);
  var authdata = userService.login(testUser);
  String token = authdata.authToken();
  userService.createGame(token,"testGame");
  Assertions.assertEquals(1,gameDao.size());
}
@Test
  void invalidList(){
  UserData testUser=new UserData(username,password,email);
  userService.register(testUser);
  userService.login(testUser);
  Assertions.assertThrows(RuntimeException.class, () -> userService.getGames(password));
}

@Test
  void clear(){
    UserData testUser=new UserData(username,password,email);
    userService.register(testUser);
   var authdata = userService.login(testUser);
  String token = authdata.authToken();
  userService.createGame(token,"testGame");
  userService.clearData();
  Assertions.assertEquals(0,gameDao.size());

}


}