import chess.*;
import ui.MakeBoard;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        MakeBoard makeBoard = new MakeBoard(new ChessBoard());
        makeBoard.renderBoard(true);
        System.out.println();
        makeBoard.renderBoard(false);
    }



}