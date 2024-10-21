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
    return calculateMoves2(board, myPosition, queenRelativeMoves);
  }
}
