package ui;
import chess.ChessBoard;
import chess.ChessPiece;


public class MakeBoard {


  ChessBoard board;

  public MakeBoard(ChessBoard board) {
    this.board = board;
    board.resetBoard();
  }

  public void renderBoard(boolean whitePerspective) {

    int startRow;
    int increment;
    int lastRow;

    if(whitePerspective){
      startRow = 7;
      increment = -1;
      lastRow = -1;
    } else{
      startRow = 0;
      increment = 1;
      lastRow = 8;
    }

    for(int start = startRow; start != lastRow; start+=increment){

      System.out.print((whitePerspective ? start+1 : start+increment) + " | ");

      for (int col = 0; col < 8; col++) {
        System.out.print(" . "); // Placeholder for now
      }
      System.out.println();
    }
    // Print column labels
    System.out.println("    a  b  c  d  e  f  g  h");

    }

  }


