package service;

import dataaccess.MemoryAuthDao;
import dataaccess.MemoryUserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.utils.Assert;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {
  String username = "testUser";
  String password = "password";
  String email = "test@g.com";
  private UserService userService; // Your service class
  private MemoryUserDao userDao; // Your DAO
  private MemoryAuthDao authDao; // Your Auth DAO if applicable

  @BeforeEach
  public void setUp() {
    userDao = new MemoryUserDao(); // Initialize your DAO
    authDao = new MemoryAuthDao(); // Initialize your Auth DAO if applicable
    userService = new UserService(userDao, authDao); // Initialize your service
  }

  @Test
  void register() {

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

}