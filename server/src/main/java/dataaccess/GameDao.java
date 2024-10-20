package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDao {

  void clear();
int createGame(String gameName) throws DataAccessException;
void  updateGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;

  ArrayList<GameData> listGames() throws DataAccessException;
}
