package ui;

import java.util.Scanner;

public class Repl {

  private final ChessClient client;

  public Repl(String serverUrl) {
    client = new ChessClient(serverUrl);
  }
  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to the Chess. Sign in or Register to start.");
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(EscapeSequences.SET_TEXT_ITALIC + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }

  private void printPrompt() {
    System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
  }

}
