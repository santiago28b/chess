package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class SQLTest {
  String username = "testUser";
  String password = "password";
  String email = "test@g.com";
  private UserService userService; // Your service class
  private SQLUserDao userDao; // Your DAO
  private SQLAuthDao authDao; // Your Auth DAO
  private SQLGameDao gameDao;

  @BeforeEach
  public void setUp() throws DataAccessException {
    userDao =new SQLUserDao(); // Initialize your DAO
    authDao =new SQLAuthDao();//
    gameDao =new SQLGameDao();
    userService = new UserService(gameDao, userDao,authDao); // Initialize your service
  }

  @Test
  void getData() {
    Assertions.assertNotNull(userDao);
  }

  @Test
  void validCreateUser() throws DataAccessException {
    UserData testUser = new UserData(username,password,email);

    userService.register(testUser);
    Assertions.assertEquals(username,userDao.getUsername(testUser));
  }
  @Test
  void failToRegister() {
    UserData testUser=new UserData(username, password, email);
    assertThrows(RuntimeException.class, () ->  userService.register(testUser));
  }

  @Test
  void createAuth() throws DataAccessException {
    UserData testUser=new UserData(username,password,email);
    String token = authDao.createAuth(testUser);
    Assertions.assertNotNull(token);
  }
  @Test
  void invalidCreateAuth() throws DataAccessException {
    UserData testUser=new UserData(null,password,email);
    assertThrows(RuntimeException.class, () ->  userService.register(testUser));
  }
  @Test
  void validGetAuth() throws DataAccessException {
    UserData testUser=new UserData(username,password,email);
    String token = authDao.createAuth(testUser);
   AuthData auth = authDao.getAuth(token);
   Assertions.assertNotNull(auth);
  }

  @Test
  void invalidGetAuth() throws DataAccessException {
    UserData testUser=new UserData(null,password,email);
  assertThrows(DataAccessException.class,() ->authDao.createAuth(testUser));
  }

  @Test
  void deleteAuth() throws DataAccessException {
    UserData testUser=new UserData(username,password,email);
    String token = authDao.createAuth(testUser);
    authDao.deleteAuth(token);
    Assertions.assertNull(authDao.getAuth(token));
  }

  @Test
  void invalidDeleteAuth() throws DataAccessException {
    UserData testUser=new UserData(username,password,email);
    authDao.createAuth(testUser);
    assertThrows(DataAccessException.class,() -> authDao.deleteAuth("fakeToken"));
  }

  @Test
  void trueValidateToken() throws DataAccessException {
    UserData testUser=new UserData(username,password,email);
    String token = authDao.createAuth(testUser);
    Assertions.assertTrue(authDao.validateToken(token));
  }
  @Test
  void falseValidateToken() throws DataAccessException {
    UserData testUser=new UserData(username,password,email);
    authDao.createAuth(testUser);
    Assertions.assertFalse(authDao.validateToken("fakeToken"));
  }
  @Test
  void createGame() throws DataAccessException {
    int id = gameDao.createGame("newGame");
    Assertions.assertEquals(1,gameDao.getGame(id).gameID());
  }
  @Test
  void invalidCreateGame() throws DataAccessException {
    assertThrows(DataAccessException.class,() -> gameDao.createGame(null));
  }

  @Test
  void updateGame() throws DataAccessException {
    int id = gameDao.createGame("newGame");
    gameDao.updateGame(id,"newUser",null,"newGame",new ChessGame());
    Assertions.assertEquals("newUser",gameDao.getGame(id).whiteUsername());
  }
  @Test
  void invalidUpdateGame() throws DataAccessException {
    int id = gameDao.createGame("newGame");
    assertThrows(DataAccessException.class,()->gameDao.updateGame(0,null,null
            ,"newGame",new ChessGame()));
  }

  @Test
  void listGames() throws DataAccessException {
    gameDao.createGame("newGame");
    assertNotNull(gameDao.listGames());
  }
  @Test
  void invalidListGames() throws DataAccessException {
    gameDao.createGame("newGame");
    UserData testUser=new UserData(username,password,email);
    assertThrows(RuntimeException.class,()-> userService.getGames("fakeToken"));
  }
  @Test
  void getGame() throws DataAccessException {
    int id = gameDao.createGame("newGame");
    assertNotNull(gameDao.getGame(id));
  }
  @Test
  void invalidGetGame() throws DataAccessException {
    gameDao.createGame("newGame");
    assertNull(gameDao.getGame(99));
  }

  @Test
  void clear() {
  }


}