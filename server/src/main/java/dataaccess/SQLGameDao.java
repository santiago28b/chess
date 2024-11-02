package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDao  extends AbstractSQLDAO implements GameDao {


  public SQLGameDao() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public void clear() throws DataAccessException {
      var statement = "TRUNCATE TABLE game";
      executeUpdate(statement);
  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    var statement = "INSERT INTO game(whiteUsername, blackUsername, gameName, game) VALUES(?,?,?,?)";
    ChessGame game = new ChessGame();
    ChessBoard board = new ChessBoard();
    board.resetBoard();
    game.setBoard(board);
    var gameJson = new Gson().toJson(game);
    return executeUpdate(statement, null,null,gameName,gameJson);
  }

  @Override
  public void updateGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
    if(gameId == 0){
      throw new DataAccessException("Game id cannot be 0");
    }
        var statement = "UPDATE game " + " SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? " +
                "WHERE gameId = ?";
        try(var conn = DatabaseManager.getConnection()){
          var ps = conn.prepareStatement(statement);
          ps.setString(1, whiteUsername);
          ps.setString(2,blackUsername);
          ps.setString(3,gameName);
          ps.setString(4, new Gson().toJson(game));
          ps.setInt(5,gameId);
          ps.executeUpdate();
        }catch (Exception e){
          throw new RuntimeException(e);
        }
  }

  @Override
  public ArrayList<GameData> listGames() throws DataAccessException {
    var games = new ArrayList<GameData>();
    try(var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement("SELECT * FROM game");
         var rs = ps.executeQuery()){
      while (rs.next()) {
        int gameID=rs.getInt("gameID");
        String whiteUsername=rs.getString("whiteUsername");
        String blackUsername=rs.getString("blackUsername");
        String gameName=rs.getString("gameName");
        String gameJson=rs.getString("game");

        ChessGame game=new Gson().fromJson(gameJson, ChessGame.class);
        GameData gameData=new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        games.add(gameData);
        }
      }catch(SQLException e){
        throw new DataAccessException("Error retrieving games: " + e.getMessage());
      }
      return games;
    }


  @Override
  public GameData getGame(int gameId) throws DataAccessException {
    GameData gamesito = null;
    var statement = "SELECT * FROM game WHERE gameID = ?";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(statement)) {
      // Set the gameID parameter in the SQL query
      ps.setInt(1, gameId);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          int ID = rs.getInt("gameId");
          String whiteUsername = rs.getString("whiteUsername");
          String blackUsername = rs.getString("blackUsername");
          String gameName = rs.getString("gameName");
          String gameJson = rs.getString("game");
          ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
          gamesito = new GameData(ID, whiteUsername, blackUsername, gameName, game);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error retrieving game: " + e.getMessage());
    }
    // Return the GameData object or null if no game was found
    return gamesito;
  }


  public int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection(); var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
      for (var i = 0; i < params.length; i++) {
        var param = params[i];
        if (param instanceof String p){ ps.setString(i + 1, p);}
        else if (param == null){ ps.setNull(i + 1, NULL);}
      }
      ps.executeUpdate();
      try (var rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          return rs.getInt(1);  // Return the generated gameID
        }
      }
      return 0;  // If no key was generated, return 0
    } catch (SQLException e) {
      throw new DataAccessException("500: Error :( " + e.getMessage());
    }
  }



}
