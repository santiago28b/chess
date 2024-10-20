package model;

import chess.ChessGame;

public record GameData(int gameId, String whiteUser, String blackUser, String gameName, ChessGame game) {

  public GameData updateGameData(String whiteUsername, String blackUsername, String gameName, ChessGame game){
    return new GameData(this.gameId(), whiteUsername, blackUsername, gameName, game);
  }
}
