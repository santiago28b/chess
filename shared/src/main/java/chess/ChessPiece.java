package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor==that.pieceColor && pieceType==that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (pieceType){
            case KING -> {
                return new KingMovesCalculator().pieceMoves(board,myPosition);
            }
            case QUEEN -> {
                return new QueenMovesCalculator().pieceMoves(board,myPosition);
            }
            case BISHOP -> {
                return new BishopMovesCalculator().pieceMoves(board,myPosition);
            }
            case ROOK -> {
                return new RookMovesCalculator().pieceMoves(board,myPosition);
            }
            case KNIGHT -> {
                return new KnightMovesCalculator().pieceMoves(board,myPosition);
            }
            case PAWN -> {
                return new PawnMovesCalculator().pieceMoves(board,myPosition);
            }
        }
        return null;
    }

    public String toString(){
        switch (pieceType){
            case KING -> {
                return pieceColor == ChessGame.TeamColor.WHITE ? ("K"):("k");
            }
            case PAWN -> {
                return pieceColor == ChessGame.TeamColor.WHITE ? ("P"):("p");
            }
            case KNIGHT -> {
                return pieceColor == ChessGame.TeamColor.WHITE ? ("N"):("n");
            }
            case ROOK -> {
                return pieceColor == ChessGame.TeamColor.WHITE ? ("R"):("r");
            }
            case BISHOP -> {
                return pieceColor == ChessGame.TeamColor.WHITE ? ("B"):("b");
            }
            case QUEEN -> {
                return pieceColor == ChessGame.TeamColor.WHITE ? ("Q"):("q");
            }
        }
        return "0";
    }
}
