package dataaccess;

public interface GameDao {

  void clear();
int createGame(String gameName) throws DataAccessException;

}
