package ui;

import chess.ChessBoard;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

  private String visitorName = null;
  private final ServerFacade server;
  private State state = State.SIGNEDOUT;
  private AuthData authData;
  Scanner scanner = new Scanner(System.in);
  private ArrayList<GameData>  listedGames = new ArrayList<>();
  private MakeBoard makeBoard; // Add this to the class as a field





  public  ChessClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
    makeBoard = new MakeBoard(new ChessBoard());


  }

  public String eval(String input) {
    var tokens = input.toLowerCase().split(" ");
    var cmd = (tokens.length > 0) ? tokens[0] : "help";
    var params = Arrays.copyOfRange(tokens, 1, tokens.length);

    if(state == State.SIGNEDOUT) {
      return switch (cmd) {
        case "login" -> login();
        case "register" -> register();
        case "help" -> help();
        case "quit" -> "quit";
        default -> help();
      };
    } else if (state == State.SIGNEDIN) {
      return switch (cmd) {
        case "creategame" -> createGame();
        case "list" -> listGames();
        case "play" -> playGame();
//        case "observegame" -> observeGame();
        case "logout" -> logout();
        case "help" -> help();
        case "quit" -> "quit";
        default -> "Invalid command. Type 'help' for available commands.";
      };
    }
    return "unknown";
  }

  private String playGame() {
    assertSignedIn();
    listGames();
    if (listedGames.isEmpty()) {
      return "No games available to join.";
    }
    int gameNumber = -1 ;
    while (gameNumber < 1 || gameNumber > listedGames.size()) {
      System.out.println("Enter the number of the game you want to join (1 to " + listedGames.size() + "): ");
      try {
        gameNumber = Integer.parseInt(scanner.nextLine());
        if (gameNumber < 1 || gameNumber > listedGames.size()) {
          System.out.println("Invalid game number. Please try again.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
    GameData selectedGame = listedGames.get(gameNumber - 1);
    System.out.println("Enter the color you want to play (WHITE or BLACK): ");
    String color = scanner.nextLine().toUpperCase();
    if (!color.equals("WHITE") && !color.equals("BLACK")) {
      return "Invalid color. Please enter 'WHITE' or 'BLACK'.";
    }
    try {
      server.joinGame(color, selectedGame.gameID(), authData);
      System.out.println("Successfully joined game '" + selectedGame.gameName() + "' as " + color + ".");
      boolean whitePerspective = color.equals("WHITE");
      makeBoard.renderBoard(whitePerspective);
      System.out.println("\n");
      makeBoard.renderBoard(!whitePerspective);
      return "Game is ready to play!";
    } catch (ServerFacade.ResponseException e) {
      return "Failed to join game: " + e.getMessage();
    }
  }


  private String listGames() {
    assertSignedIn();
    ArrayList<GameData> games =null;
    try {
      games=server.listGames(authData);
    } catch (ServerFacade.ResponseException e) {
      return "there was an error " + e.getMessage();
    }
    if(games.isEmpty()) {
      return "No games found";
    } else{
      listedGames = games;
      StringBuilder result = new StringBuilder("List of Available Games:\n");
      int gameCount = 1;
     for(GameData game: games) {
       result.append(gameCount).append(". ")
               .append("Game Name: ").append(game.gameName()).append(" -- ")
               .append("White Player: ").append(game.whiteUsername() != null ?
                       game.whiteUsername() : "No player yet").append(" -- ")
               .append("Black Player: ").append(game.blackUsername() != null ?
                       game.blackUsername() : "No player yet")
               .append("\n");
       gameCount++;
     }
     return result.toString();
    }

  }

  private String createGame() {
    assertSignedIn();
    System.out.println("Choose a name for the game: ");
    String gameName = scanner.nextLine();
    assertNotNull(gameName);
    try{
      server.createGame(gameName,authData);
    } catch (ServerFacade.ResponseException e) {
      return "there was an error " + e.getMessage();
    }
      return gameName + " has been created";
  }


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

  private void assertNotNull(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new RuntimeException("You must provide a name");
    }
  }

  private String login() {
    System.out.println("Username: ");
    String username = scanner.nextLine();
    System.out.println("Password: ");
    String password = scanner.nextLine();
    UserData  existent = new UserData(username,password,null);
    try{
      authData = server.login(existent);
      state = State.SIGNEDIN;
      visitorName = username;
      return "Login successful! Welcome, " + visitorName;
    } catch (ServerFacade.ResponseException e) {
      return "login failed, probably invalid credentials. " + e.getMessage();
    }
  }

  private String register() {
    System.out.println("New username: ");
    String username = scanner.nextLine();
    System.out.println("New password: ");
    String password = scanner.nextLine();
    System.out.println("Email: ");
    String email = scanner.nextLine();
    if(username == null || password == null) {
      return "credentials can't be null";
    } else if (username.trim().isEmpty() || password.trim().isEmpty()) {
      return "username or password can't be empty";
    }

    try{
      UserData newUser = new UserData(username, password, email);
      server.register(newUser);
      state = State.SIGNEDIN;
      visitorName = username;
      return "Registration successful! You are now signed in as " + visitorName;
    } catch (ServerFacade.ResponseException e) {
      return "Registration failed, probably invalid credentials. " + e.getMessage();
    }
  }

//  private void displayMenu() {
//    switch (state) {
//      case SIGNEDIN:
//        System.out.println("\nYou are signed in as " + visitorName + ".");
//        System.out.println("Available commands:");
//        System.out.println("- startgame: Start a new game.");
//        System.out.println("- listgames: List active games.");
//        System.out.println("- logout: Sign out of your account.");
//        System.out.println("- quit: Exit the application.");
//        System.out.println("- help: Show this menu.");
//
//      case SIGNEDOUT:
//        System.out.println("Register");
//        System.out.println("login");
//        System.out.println("quit");
//        System.out.println("help");
//    }
//  }

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
