package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator{
  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

    switch (board.getPiece(myPosition).getTeamColor()){
      case WHITE -> {
        return new PawnMovesCalculator().calculatePawnMoves(board,myPosition,1,8,2);
      }
      case BLACK -> {
        return new PawnMovesCalculator().calculatePawnMoves(board,myPosition,-1,1,7);
      }
    }
    return null;
  }
  private Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition myPosition, int direction, int prom, int startR){
    HashSet<ChessMove> validMoves = new HashSet<>();
    int row = myPosition.getRow();
    int col = myPosition.getColumn();

    // Move forward
    ChessPosition newPosition = new ChessPosition(row + direction, col);
    if (isValidMove(newPosition) && board.getPiece(newPosition) == null) {
      if (newPosition.getRow() == prom) {
        addPromotion(validMoves, myPosition, newPosition);
      } else {
        validMoves.add(new ChessMove(myPosition, newPosition, null));
      }
    }

    // Double move from starting position
    if (row == startR) {
      newPosition = new ChessPosition(row + 2 * direction, col);
      ChessPosition intermediate = new ChessPosition(row + direction, col);
      if (isValidMove(newPosition) && board.getPiece(intermediate) == null && board.getPiece(newPosition) == null) {
        validMoves.add(new ChessMove(myPosition, newPosition, null));
      }
    }

    // Capture moves
    int[][] captureMoves = {
            {direction, 1}, // Capture right
            {direction, -1} // Capture left
    };

    for (int[] move : captureMoves) {
      newPosition = new ChessPosition(row + move[0], col + move[1]);
      if (isValidMove(newPosition) && board.getPiece(newPosition) != null) {
        if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
          if (newPosition.getRow() == prom) {
            addPromotion(validMoves, myPosition, newPosition);
          } else {
            validMoves.add(new ChessMove(myPosition, newPosition, null));
          }
        }
      }
    }
    return validMoves;
  }

  private void addPromotion(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessPosition newPosition) {
    validMoves.add(new ChessMove(myPosition,newPosition,ChessPiece.PieceType.QUEEN));
    validMoves.add(new ChessMove(myPosition,newPosition,ChessPiece.PieceType.KNIGHT));
    validMoves.add(new ChessMove(myPosition,newPosition,ChessPiece.PieceType.BISHOP));
    validMoves.add(new ChessMove(myPosition,newPosition,ChessPiece.PieceType.ROOK));
  }
}
