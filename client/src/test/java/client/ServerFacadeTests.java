package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server=new Server();
        var port=server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl="http://localhost:" + port;
        serverFacade=new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterSuccess() {
        // Arrange
        UserData newUser=new UserData("testUser", "testPassword", "testUser@example.com");

        // Act
        try {
            AuthData authData=serverFacade.register(newUser);

            // Assert
            assertNotNull(authData, "AuthData should not be null");
            assertNotNull(authData.authToken(), "Auth token should not be null");
            System.out.println("Registration successful with token: " + authData.authToken());
        } catch (Exception e) {
            fail("Registration failed: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterDuplicateUser() throws ServerFacade.ResponseException {
        // Arrange
        UserData duplicateUser=new UserData("new", "password", "duplicateUser@example.com");
        serverFacade.register(duplicateUser); // Register the user first
        assertThrows(ServerFacade.ResponseException.class, () -> {
            serverFacade.register(duplicateUser); // Try registering the same user again
        });
    }

    @Test
    public void testLoginSuccess() throws ServerFacade.ResponseException {
        UserData newUser=new UserData("testUser", "testPassword", "testUser@example.com");
        AuthData authData=serverFacade.login(newUser);
        assertNotNull(authData, "AuthData should not be null");
        assertNotNull(authData.authToken(), "Auth token should not be null");
        System.out.println("Login successful with token: " + authData.authToken());
    }

    @Test
    public void invalidLogin() throws ServerFacade.ResponseException {
        UserData newUser=new UserData("paila", "testPassword", "testUser@example.com");
        assertThrows(ServerFacade.ResponseException.class, () -> {
            serverFacade.login(newUser);
        });
    }
    @Test
    public void validLogout() throws ServerFacade.ResponseException {
        UserData newUser=new UserData("testUser", "testPassword", "testUser@example.com");
        AuthData authData=serverFacade.login(newUser);
        assertNotNull(authData, "AuthData should not be null");
        assertNotNull(authData.authToken(), "Auth token should not be null");
        serverFacade.logout(authData);
    }
    @Test
    public void invalidLogout() throws ServerFacade.ResponseException {
        assertThrows(ServerFacade.ResponseException.class, () -> {
            serverFacade.logout(null);
        });
    }
    @Test
    public void validCreate() throws ServerFacade.ResponseException {
        UserData newUser=new UserData("testUser", "testPassword", "testUser@example.com");
        AuthData authData=serverFacade.login(newUser);
        GameData newGame = serverFacade.createGame("newGame", authData);
        assertNotNull(newGame, "Game should not be null");
    }
    @Test void invalidCreate() throws ServerFacade.ResponseException {
        assertThrows(ServerFacade.ResponseException.class, () -> {
            serverFacade.createGame("newGame", null);
        });
    }



}
