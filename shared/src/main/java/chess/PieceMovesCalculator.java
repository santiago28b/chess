package chess;

import java.util.Collection;

public abstract class PieceMovesCalculator {

  public abstract Collection<ChessMove>pieceMoves(ChessBoard board,ChessPosition myPosition);

  boolean isValidMove(ChessPosition position){
    if(position.getRow()>=1 && position.getRow()<=8){
      if(position.getColumn()>=1 && position.getColumn()<=8){
        return true;
      }
    }
    return false;
  }
}
