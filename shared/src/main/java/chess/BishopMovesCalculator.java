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
    return calculateMoves2(board, myPosition, bishopMoves);
  }
}
