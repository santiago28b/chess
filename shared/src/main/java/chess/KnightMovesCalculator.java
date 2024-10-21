package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    int[][] relativeKnightMoves = {
            {1,2},//up
            {1,-2},
            {-1,2},
            {-1,-2},
            {2,1},
            {2,-1},
            {-2,-1},
            {-2,1}
    };

    return calculateMoves(board, myPosition, relativeKnightMoves);
  }
}
