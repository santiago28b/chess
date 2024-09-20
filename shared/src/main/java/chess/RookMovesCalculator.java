package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends PieceMovesCalculator{
  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int[][] relativeRookMoves = {
            {1,0},//up
            {0,1},//right
            {-1,0},//down
            {0,-1},//left
    };

    HashSet<ChessMove> validMoves = new HashSet<>();

    int row = myPosition.getRow();
    int col = myPosition.getColumn();

    for(int i = 0; i <relativeRookMoves.length; i++){
      ChessPosition newPosition;
      boolean obstruction = false;
      int j = 1;

      while(!obstruction){
        int newRow;
        int newCol;
        newRow = row + relativeRookMoves[i][0]*j;
        newCol = col + relativeRookMoves[i][1]*j;

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
