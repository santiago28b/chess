package ui;

import java.util.Scanner;

public class Repl {

  private final ChessClient client;

  public Repl(String serverUrl) {
    client = new ChessClient(serverUrl);
  }
  public void run() {
    System.out.println(EscapeSequences.SET_TEXT_BOLD + "Welcome to Chess. Sign in or Register to start." + EscapeSequences.RESET_TEXT_COLOR);
    System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN + client.help() + EscapeSequences.RESET_TEXT_COLOR);

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }

}
