package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator extends PieceMovesCalculator{
  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int[][] queenRelativeMoves = {
            {1,0},//up
            {1,1},//up-right
            {0,1},//right
            {-1,1},//down right
            {-1,0},//down
            {-1,-1},//down-left
            {0,-1},//left
            {1,-1}//up-left
    };

    HashSet<ChessMove> validMoves = new HashSet<>();

    int row = myPosition.getRow();
    int col =myPosition.getColumn();

    for(int i =0; i <queenRelativeMoves.length; i++){
      ChessPosition newPosition;
      boolean obstruction = false;
        int j = 1;
        while(!obstruction){
          int newRow = row + queenRelativeMoves[i][0]*j;
          int newCol = col + queenRelativeMoves[i][1]*j;

          newPosition = new ChessPosition(newRow,newCol);
          if(isValidMove(newPosition)){
            if(board.getPiece(newPosition)==null){
              validMoves.add(new ChessMove(myPosition,newPosition,null));
              j++;
            } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
              validMoves.add(new ChessMove(myPosition,newPosition,null));
              obstruction=true;
            } else{
              obstruction = true;
            }
          } else {
            obstruction = true;
          }
        }
    }

    return validMoves;
  }
}
