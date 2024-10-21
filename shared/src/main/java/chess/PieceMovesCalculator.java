package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class PieceMovesCalculator {

  public abstract Collection<ChessMove>pieceMoves(ChessBoard board,ChessPosition myPosition);

  boolean isValidMove(ChessPosition position){
    if(position.getRow()>=1 && position.getRow()<=8){
      if(position.getColumn()>=1 && position.getColumn()<=8){
        return true;
      }
    }
    return false;
  }

  protected Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, int[][] relativeMoves) {

    HashSet<ChessMove> validMoves = new HashSet<>();
    int row = myPosition.getRow();
    int col =myPosition.getColumn();


    for (int i =0; i<relativeMoves.length; i++){
      ChessPosition newPosition;
      int newRow;
      int newCol;

      newRow = row + relativeMoves[i][0];
      newCol = col + relativeMoves[i][1];

      newPosition = new ChessPosition(newRow,newCol);
      if(isValidMove(newPosition)){
        if(board.getPiece(newPosition)== null){
          validMoves.add(new ChessMove(myPosition,newPosition,null));
        } else if (board.getPiece(newPosition).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
          validMoves.add(new ChessMove(myPosition,newPosition,null));

        }
      }

    }
    return validMoves;
  }

  protected Collection<ChessMove> calculateMoves2(ChessBoard board, ChessPosition myPosition, int[][] relativeMoves) {
    HashSet<ChessMove> validMoves = new HashSet<>();

    int row = myPosition.getRow();
    int col = myPosition.getColumn();

    for(int i = 0; i <relativeMoves.length; i++){
      ChessPosition newPosition;
      boolean obstruction = false;
      int j = 1;

      while(!obstruction){
        int newRow;
        int newCol;
        newRow = row + relativeMoves[i][0]*j;
        newCol = col + relativeMoves[i][1]*j;

        newPosition = new ChessPosition(newRow,newCol);
        if(isValidMove(newPosition)){
          if(board.getPiece(newPosition) == null){
            validMoves.add(new ChessMove(myPosition,newPosition,null));
            j++;
          }
          else if(board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            validMoves.add(new ChessMove(myPosition,newPosition,null));
            obstruction = true;
          }
          else{
            obstruction = true;
          }
        }
        else{
          obstruction = true;
        }
      }
    }
    return validMoves;
  }




  }
