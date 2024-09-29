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
        HashSet<ChessMove> pieceMoves=new HashSet<>();
        if (board.getPiece(move.getStartPosition())==null) {
            throw new InvalidMoveException("position is null");
        }
        ChessPiece piece=board.getPiece(move.getStartPosition());

        if (!piece.getTeamColor().equals(getTeamTurn())) {
            throw new InvalidMoveException("Not your turn");
        } else {
            pieceMoves.addAll(validMoves(move.getStartPosition()));
            if (pieceMoves.contains(move)) {
                if (piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
                    if (move.getPromotionPiece()!=null) {
                        ChessPiece promotion=new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                        board.addPiece(move.getEndPosition(), promotion);
                        board.addPiece(move.getStartPosition(), null);
                        if (piece.getTeamColor().equals(TeamColor.WHITE)) {
                            setTeamTurn(TeamColor.BLACK);
                        } else {
                            setTeamTurn(TeamColor.WHITE);
                        }
                    } else {
                        board.addPiece(move.getEndPosition(), piece);
                        board.addPiece(move.getStartPosition(), null);
                        if (piece.getTeamColor().equals(TeamColor.WHITE)) {
                            setTeamTurn(TeamColor.BLACK);
                        } else {
                            setTeamTurn(TeamColor.WHITE);
                        }

                    }
                } else {
                    board.addPiece(move.getEndPosition(), piece);
                    board.addPiece(move.getStartPosition(), null); //fix to show the promition piece
                    if (piece.getTeamColor().equals(TeamColor.WHITE)) {
                        setTeamTurn(TeamColor.BLACK);
                    } else {
                        setTeamTurn(TeamColor.WHITE);
                    }
                }

            } else {
                throw new InvalidMoveException("invalid move");
            }
        }
    }

        /**
         * Determines if the given team is in check
         *
         * @param teamColor which team to check for check
         * @return True if the specified team is in check
         */
        public boolean isInCheck (TeamColor teamColor){
            ChessPosition kingPosition = findKing(teamColor);
            if(kingPosition == null){
                return false;
            }
            ChessPosition pieceOppositePosition = null;
            for(int row = 1; row <= 8; row++){
                for (int col = 1; col <= 8; col++){
                    ChessPosition possible = new ChessPosition(row,col);
                    if(board.getPiece(possible)!= null){  //finding opponent pieces that may put king in Check
                        if(board.getPiece(possible).getTeamColor() != board.getPiece(kingPosition).getTeamColor()){
                            pieceOppositePosition = new ChessPosition(row,col);
                            ChessPiece opposite = new ChessPiece(board.getPiece(pieceOppositePosition).getTeamColor(),board.getPiece(pieceOppositePosition).getPieceType());
                            Collection<ChessMove> validMoves = opposite.pieceMoves(board,pieceOppositePosition);
                            for (ChessMove move : validMoves){
                                if(move.getEndPosition().equals(kingPosition)){
                                    return true;
                                }
                            }
                        }
                    }
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
            if (!isInCheck(teamColor)) {
                return false;
            }
            HashSet<ChessMove> saveTheKingMoves=new HashSet<>();
            for (int row=1; row<=8; row++) {
                for (int col=1; col<=8; col++) {
                    ChessPosition position=new ChessPosition(row, col);
                    if (board.getPiece(position)!=null && board.getPiece(position).getTeamColor().equals(teamColor)) {
                        saveTheKingMoves.addAll(validMoves(position));
                    }
                }
            }
            return saveTheKingMoves.isEmpty();
        }

        /**
         * Determines if the given team is in stalemate, which here is defined as having
         * no valid moves
         *
         * @param teamColor which team to check for stalemate
         * @return True if the specified team is in stalemate, otherwise false
         */
        public boolean isInStalemate (TeamColor teamColor){
            if (isInCheck(teamColor)) {
                return false;
            }
            HashSet<ChessMove> saveStaleMate=new HashSet<>();
            for (int row=1; row<=8; row++) {
                for (int col=1; col<=8; col++) {
                    ChessPosition position=new ChessPosition(row, col);
                    if (board.getPiece(position)!=null && board.getPiece(position).getTeamColor().equals(teamColor)) {
                        saveStaleMate.addAll(validMoves(position));
                    }
                }
            }
            return saveStaleMate.isEmpty(); //double check this one
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
