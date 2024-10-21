package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        teamTurn = TeamColor.WHITE;
        board.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> realMoves=new HashSet<>();
        if (board.getPiece(startPosition)==null) {
            return null;
        } else {

            ChessPiece pieceToMove=board.getPiece(startPosition);
            ChessBoard copyboard=boardCopy(board);
            // end of copying board
            ChessBoard original=boardCopy(board);

            for (ChessMove move : pieceToMove.pieceMoves(copyboard, startPosition)) {
                this.board=copyboard;
                simulateMove(copyboard, move);
                boolean inCheck=isInCheck(pieceToMove.getTeamColor());
                copyboard=boardCopy(original);
                this.board=boardCopy(original);
                if (!inCheck) {
                    realMoves.add(move);
                }
            }// end of for loop
        }
        //filter moves so have actual moves that do not put the king in danger.
        return realMoves;
        //second
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        validateMove(move);
        ChessPiece piece = board.getPiece(move.getStartPosition());
        performMove(move, piece);
    }

    private void validateMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Position is null");
        }
        if (!board.getPiece(move.getStartPosition()).getTeamColor().equals(getTeamTurn())) {
            throw new InvalidMoveException("Not your turn");
        }
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }
    }

    private void performMove(ChessMove move, ChessPiece piece) {
        if (piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
            handlePawnMove(move, piece);
        } else {
            movePiece(move, piece);
        }
    }
    private void handlePawnMove(ChessMove move, ChessPiece piece) {
        if (move.getPromotionPiece() != null) {
            promotePawn(move, piece);
        } else {
            movePiece(move, piece);
        }
    }
    private void promotePawn(ChessMove move, ChessPiece piece) {
        ChessPiece promotion = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        movePiece(move, promotion);
    }

    private void movePiece(ChessMove move, ChessPiece piece) {
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
        switchTurn();
    }

    private void switchTurn() {
        setTeamTurn(teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

        /**
         * Determines if the given team is in check
         *
         * @param teamColor which team to check for check
         * @return True if the specified team is in check
         */
        public boolean isInCheck (TeamColor teamColor){
            ChessPosition kingPosition = findKing(teamColor);
            if (kingPosition == null) return false; // If the king isn't found, it's not in check

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition possible = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(possible);
                    if (piece != null && piece.getTeamColor() != teamColor) {
                        // Check if this opponent's piece can attack the king
                        if (canAttackKing(possible, kingPosition, piece)) {
                            return true; // The king is in check
                        }
                    }
                }
            }
            return false; // The ki
        }

    private boolean canAttackKing(ChessPosition attackerPosition, ChessPosition kingPosition, ChessPiece attacker) {
        Collection<ChessMove> validMoves = attacker.pieceMoves(board, attackerPosition);

        // Check if any valid move ends at the king's position
        for (ChessMove move : validMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true; // The attacker can move to the king's position
            }
        }
        return false;
    }

        /**
         * Determines if the given team is in checkmate
         *
         * @param teamColor which team to check for checkmate
         * @return True if the specified team is in checkmate
         */
        public boolean isInCheckmate (TeamColor teamColor){
            if (!isInCheck(teamColor)) return false;

            HashSet<ChessMove> possibleMoves = getAllPossibleMoves(teamColor);
            return possibleMoves.isEmpty();
        }

        /**
         * Determines if the given team is in stalemate, which here is defined as having
         * no valid moves
         *
         * @param teamColor which team to check for stalemate
         * @return True if the specified team is in stalemate, otherwise false
         */
        public boolean isInStalemate (TeamColor teamColor){
            return !isInCheck(teamColor) && getAllPossibleMoves(teamColor).isEmpty();
        }
    private HashSet<ChessMove> getAllPossibleMoves(TeamColor teamColor) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor().equals(teamColor)) {
                    possibleMoves.addAll(validMoves(position));
                }
            }
        }
        return possibleMoves;
    }

        /**
         * Sets this game's chessboard with a given board
         *
         * @param board the new board to use
         */
        public void setBoard (ChessBoard board){

            this.board=board;
        }

        /**
         * Gets the current chessboard
         *
         * @return the chessboard
         */
        public ChessBoard getBoard () {
            return board;
        }
        private ChessPosition findKing (TeamColor color){
            ChessPosition kingPosition=null;
            for (int row=1; row<=8; row++) {
                for (int col=1; col<=8; col++) {
                    ChessPosition possible=new ChessPosition(row, col);
                    ChessPiece piece=board.getPiece(possible);
                    if (board.getPiece(possible)!=null) {
                        if (piece.getTeamColor().equals(color) && piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                            kingPosition=possible;
                            return kingPosition;
                        }
                    }
                }
            }
            return null;
        }
        private void simulateMove (ChessBoard boardCopy, ChessMove move){
            ChessPiece piece=boardCopy.getPiece(move.getStartPosition());
            boardCopy.addPiece(move.getStartPosition(), null); //moving it from original position
            boardCopy.addPiece(move.getEndPosition(), piece);
        }

        private ChessBoard boardCopy (ChessBoard board){
            ChessBoard copy=new ChessBoard();
            for (int row=1; row<=8; row++) {
                for (int col=1; col<=8; col++) {
                    ChessPosition position=new ChessPosition(row, col);
                    ChessPiece piece=board.getPiece(position);
                    if (piece!=null) {
                        ChessPiece newPiece=new ChessPiece(piece.getTeamColor(), piece.getPieceType());
                        copy.addPiece(position, newPiece);
                    }
                }
            }// end of copying board
            return copy;
        }
    }
