package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterSuccess() {
        // Arrange
        UserData newUser = new UserData("testUser", "testPassword", "testUser@example.com");

        // Act
        try {
            AuthData authData = serverFacade.register(newUser);

            // Assert
            assertNotNull(authData, "AuthData should not be null");
            assertNotNull(authData.authToken(), "Auth token should not be null");
            System.out.println("Registration successful with token: " + authData.authToken());
        } catch (Exception e) {
            fail("Registration failed: " + e.getMessage());
        }
    }

}
