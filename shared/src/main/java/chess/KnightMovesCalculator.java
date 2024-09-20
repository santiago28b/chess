package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int[][] realtiveKnightMoves = {
            {1,2},//up
            {1,-2},
            {-1,2},
            {-1,-2},
            {2,1},
            {2,-1},
            {-2,-1},
            {-2,1}
    };

    HashSet<ChessMove> validMoves = new HashSet<>();
    int row = myPosition.getRow();
    int col =myPosition.getColumn();

    for (int i =0; i<realtiveKnightMoves.length; i++){
      ChessPosition newPosition;
      int newRow;
      int newCol;

      newRow = row + realtiveKnightMoves[i][0];
      newCol = col + realtiveKnightMoves[i][1];

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
}
