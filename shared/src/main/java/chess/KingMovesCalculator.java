package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends PieceMovesCalculator {
  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int[][] realtiveKingMoves = {
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

    for (int i =0; i<realtiveKingMoves.length; i++){
      ChessPosition newPosition;
      int newRow;
      int newCol;

      newRow = row + realtiveKingMoves[i][0];
      newCol = col + realtiveKingMoves[i][1];

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
