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
    return calculateMoves2(board, myPosition, relativeRookMoves);
  }
}
