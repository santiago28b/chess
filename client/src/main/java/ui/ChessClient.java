package ui;

import model.UserData;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

  private String visitorName = null;
  private final ServerFacade server;
  //private final String serverUrl;
  private State state = State.SIGNEDOUT;
  Scanner scanner = new Scanner(System.in);




  public  ChessClient(String serverUrl) {
    server = new ServerFacade(serverUrl);

  }

  public String eval(String input) {
    var tokens = input.toLowerCase().split(" ");
    var cmd = (tokens.length > 0) ? tokens[0] : "help";
    var params = Arrays.copyOfRange(tokens, 1, tokens.length);
    return switch (cmd) {
      case "login" -> login();
      case "register" -> register();
//        case "list" -> listPets();
        case "logout" -> logout();
//        case "adopt" -> adoptPet(params);
//        case "adoptall" -> adoptAllPets();
        case "quit" -> "quit";
      default -> help();
    };
  }

//  public void start() {
//    displayPreLoginMenu();
//  }

  private String logout() {
    assertSignedIn();
    state = State.SIGNEDOUT;
    return String.format("%s see ya!", visitorName);
  }

  private void assertSignedIn() {
    if (state == State.SIGNEDOUT) {
      throw new RuntimeException("You must sign in");
    }
  }

  private String login() {
    System.out.println("Username: ");
    String username = scanner.nextLine();
    System.out.println("Password: ");
    String password = scanner.nextLine();
    UserData  existent = new UserData(username,password,null);
    try{
      var authdata = server.login(existent);
      state = State.SIGNEDIN;
      visitorName = username;
      return "Login successful! Welcome, " + visitorName;
    } catch (ServerFacade.ResponseException e) {
      throw new RuntimeException(e);
    }
  }

  private String register() {
    System.out.println("New username: ");
    String username = scanner.nextLine();
    System.out.println("New password: ");
    String password = scanner.nextLine();
    System.out.println("Email: ");
    String email = scanner.nextLine();

    try{
      UserData newUser = new UserData(username, password, email);
      server.register(newUser);
      state = State.SIGNEDIN;
      visitorName = username;
      return "Registration successful! You are now signed in as " + visitorName;
    } catch (ServerFacade.ResponseException e) {
      throw new RuntimeException(e);
    }
  }



  private void displayMenu() {
    switch (state) {
      case SIGNEDIN:
        System.out.println("\nYou are signed in as " + visitorName + ".");
        System.out.println("Available commands:");
        System.out.println("- startgame: Start a new game.");
        System.out.println("- listgames: List active games.");
        System.out.println("- logout: Sign out of your account.");
        System.out.println("- quit: Exit the application.");
        System.out.println("- help: Show this menu.");

      case SIGNEDOUT:
        System.out.println("Register");
        System.out.println("login");
        System.out.println("quit");
        System.out.println("help");
    }
  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      return """
                    - Register
                    - Login
                    - Quit
                    - Help
                    """;
    }
    return """
                - list games
                - Observe game
                - list game
                - create Game
                - logout
                - join Game
                - quit
                """;
  }

}
