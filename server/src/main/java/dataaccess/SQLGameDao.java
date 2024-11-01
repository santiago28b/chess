package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDao implements GameDao {
  @Override
  public void clear() {

  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    return 0;
  }

  @Override
  public void updateGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {

  }

  @Override
  public ArrayList<GameData> listGames() throws DataAccessException {
    return null;
  }

  @Override
  public GameData getGame(int gameID) {
    return null;
  }
}
