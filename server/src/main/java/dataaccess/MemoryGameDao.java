package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDao implements GameDao {
  private ArrayList<GameData> games = new ArrayList<>();
  private int gameId = 1;


  @Override
  public void clear() {

  }

  @Override
  public int createGame(String gameName) throws DataAccessException{
    GameData newGame = new GameData(gameId,null,null,gameName,new ChessGame());
    games.add(newGame);
    return gameId++;
  }

}
