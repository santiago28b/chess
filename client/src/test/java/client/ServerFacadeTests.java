package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.ArrayList;

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

    @BeforeEach
    @Test
    public void clear() throws ServerFacade.ResponseException {
        serverFacade.clear();
        UserData newUser=new UserData("testUser", "testPassword", "testUser@example.com");
        serverFacade.register(newUser);
        AuthData authData = serverFacade.login(newUser);
        ArrayList<GameData> games = serverFacade.listGames(authData);
        assertEquals(0,games.size());

    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterSuccess() throws ServerFacade.ResponseException {
        // Arrange
        UserData newUser=new UserData("newUser", "testPassword", "testUser@example.com");
            AuthData authData=serverFacade.register(newUser);
            assertNotNull(authData, "AuthData should not be null");
            assertNotNull(authData.authToken(), "Auth token should not be null");
            System.out.println("Registration successful with token: " + authData.authToken());

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

    @Test
    public void listGames() throws ServerFacade.ResponseException {
        UserData newUser=new UserData("testUser", "testPassword", "testUser@example.com");
        AuthData authData=serverFacade.login(newUser);
       serverFacade.createGame("newGame", authData);
       ArrayList<GameData> games=serverFacade.listGames(authData);
       assertNotNull(games, "Game should not be null");
       games.forEach(System.out::println);
    }
    @Test
    public void invalidListGames() throws ServerFacade.ResponseException {
        assertThrows(ServerFacade.ResponseException.class, () -> {
            serverFacade.listGames(null);
        });
    }
    @Test
    public void joinGame() throws ServerFacade.ResponseException {
        UserData newUser = new UserData("testUser", "testPassword", "testUser@example.com");
        AuthData authData = serverFacade.login(newUser);
        // Precondition: Ensure a game exists
        GameData createdGame = serverFacade.createGame("testGame", authData);
        int gameId = createdGame.gameID(); // Assuming GameData has getGameId()
        // Act: Join the game
        serverFacade.joinGame("WHITE", gameId, authData);
        // Assert: Validate the user joined the game
        ArrayList<GameData> games = serverFacade.listGames(authData);
        boolean found = false;
        for (GameData game : games) {
            if (game.gameID() == gameId) {
                assertEquals(game.whiteUsername(), newUser.username(), "The white player should match the logged-in user");
                found = true;
            }
        }
        assertTrue(found, "Game with the correct ID should be found in the user's game list");
    }
    @Test
    public void invalidJoinGame() throws ServerFacade.ResponseException {
        assertThrows(ServerFacade.ResponseException.class, () -> {
            serverFacade.joinGame("WHITE",1,null);
        });
    }




}
