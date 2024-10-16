package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDao;
import dataaccess.MemoryUserDao;
import model.UserData;
import service.UserService;
import spark.*;

import java.util.Map;

public class Server {

  MemoryUserDao userDao = new MemoryUserDao();
  MemoryAuthDao authDao = new MemoryAuthDao();
  private UserService userService = new UserService(userDao,authDao);

  public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
      Spark.post("/user", this::registerRequest);


      //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

  public  Object registerRequest(Request request, Response response) {
    UserData user  =(UserData) deserialize(request);

    try{
      var authData = userService.register(user);
      response.status(200);
      response.type("application/json");
      return new Gson().toJson(authData);
    } catch(DataAccessException e){
      if(user.password() == null||user.email()== null){
        response.status(400);
      }else {
        response.status(403);
      }
      response.type("application/json");
      return new Gson().toJson(Map.of("message", e.getMessage()));
    }
  }


  private  Object deserialize(Request request){
    return new Gson().fromJson(request.body(), UserData.class);
  }


  public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
