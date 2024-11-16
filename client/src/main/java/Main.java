import chess.*;
import ui.MakeBoard;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

//        MakeBoard makeBoard = new MakeBoard(new ChessBoard());
//        makeBoard.renderBoard(true);
//        System.out.println();
//        makeBoard.renderBoard(false);
        var serverUrl = "http://localhost:8080";
        if(args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
    }



}