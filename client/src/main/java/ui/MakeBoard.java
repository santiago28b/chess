package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;


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
       String squareColor = ((start + col) % 2 == 0) ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY :
               EscapeSequences.SET_BG_COLOR_DARK_GREY;

       ChessPiece piece = board.getPiece(new ChessPosition(start+1,col+1));
       String pieceSymbol = putPiece(piece);

       System.out.print(squareColor + pieceSymbol + EscapeSequences.RESET_BG_COLOR);
      }
      System.out.println();
    }
    // Print column labels
    System.out.println("     a  b  c  d  e  f  g  h");

    }

    private String putPiece(ChessPiece piece){
      if (piece == null){return EscapeSequences.EMPTY;}
        switch (piece.getPieceType()){
          case KING:
            return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING: EscapeSequences.BLACK_KING;
            case QUEEN:
              return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN: EscapeSequences.BLACK_QUEEN;
          case BISHOP:
            return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP: EscapeSequences.BLACK_BISHOP;
            case KNIGHT:
              return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT: EscapeSequences.BLACK_KNIGHT;
              case PAWN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN: EscapeSequences.BLACK_PAWN;
          case ROOK:
            return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK: EscapeSequences.BLACK_ROOK;
          default:
            return EscapeSequences.EMPTY;
        }
    }

  }


