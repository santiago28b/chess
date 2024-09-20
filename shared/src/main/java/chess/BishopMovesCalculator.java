package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends PieceMovesCalculator {
  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int[][] bishopMoves = {
            {1,1},//up right
            {1,-1},//up left
            {-1,-1},//down left
            {-1,1}, //down right
    };

    HashSet<ChessMove> validMoves = new HashSet<>();
    int row = myPosition.getRow();
    int col = myPosition.getColumn();

    for(int i = 0; i <bishopMoves.length; i++){
      boolean obstruction = false;
      int j = 1;
      while(!obstruction){
        int newRow = row + bishopMoves[i][0]*j;
        int newCol = col + bishopMoves[i][1]*j;

        ChessPosition newPosition = new ChessPosition(newRow,newCol);
        if(isValidMove(newPosition)){
          if(board.getPiece(newPosition)== null){
            validMoves.add(new ChessMove(myPosition,newPosition,null));
            j++;
          } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            validMoves.add(new ChessMove(myPosition,newPosition,null));
            obstruction = true;
          } else{
            obstruction = true;
          }
        } else{
          obstruction = true;
        }

      }

    }

    return validMoves;
  }
}
