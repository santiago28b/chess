package model;

import chess.ChessGame;

public record GameData(int gameId, String whiteUser, String blackUser, String gameName, ChessGame game) {
}
