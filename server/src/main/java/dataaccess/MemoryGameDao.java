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

  public GameData getGame(int id){
    for(int i =0 ; i < games.size(); i++){
      if(games.get(i).gameId() == id){
        return games.get(i);
      }
    }
    return null;
  }

  public void updateGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException{
    for (int i = 0; i < games.size(); i++){
      if (games.get(i).gameId() == gameId){
        games.set(i, games.get(i).updateGameData(whiteUsername, blackUsername, gameName, game));
      }
    }
  }

  @Override
  public ArrayList<GameData> listGames() throws DataAccessException {
    return games;
  }


}
