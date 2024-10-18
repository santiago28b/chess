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
  private final Gson gson = new Gson();
  private static final int HTTP_OK = 200;
  private static final int HTTP_FORBIDDEN = 403;
  private static final int HTTP_BAD_REQUEST = 400;
  private static final int HTTP_UNAUTHORIZED = 401;
  private static final int HTTP_ERROR = 500;




  public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
      Spark.post("/user", this::registerRequest);
      Spark.post("/session",this::loginRequest);
      Spark.delete("/session",this::logoutRequest);
      Spark.delete("/db",this::clear);



    //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

  private Object logoutRequest(Request request, Response response) {
    String token = request.headers("authorization");
    try {
      userService.logoutUser(token);
      return createResponse(response,HTTP_OK,Map.of("message", "Logged out successfully"));
    }catch (RuntimeException e){
      return createErrorResponse(response,HTTP_UNAUTHORIZED,e.getMessage());
    }
  }

  private Object loginRequest(Request request, Response response) {
    UserData loginUser = gson.fromJson(request.body(), UserData.class);
    try{
      var authData = userService.login(loginUser);
     return createResponse(response,HTTP_OK,authData);
    }catch (RuntimeException e){
      return createErrorResponse(response,HTTP_UNAUTHORIZED,e.getMessage());

    }
  }

  public  Object registerRequest(Request request, Response response) {
    UserData user = gson.fromJson(request.body(), UserData.class);
    try{
      var authData = userService.register(user);
      return createResponse(response,HTTP_OK,authData);
    } catch(RuntimeException e){
      return handleRegistrationError(response,user,e);
    }
  }
  private Object clear(Request request, Response response) {
    try{
      userService.clearData();
      return  createResponse(response,HTTP_OK,Map.of("status", "success"));
    } catch (RuntimeException e){
      return  createErrorResponse(response,HTTP_ERROR, e.getMessage());
    }
  }

  private Object createResponse(Response response, int statusCode, Object data) {
    response.status(statusCode);
    response.type("application/json");
    return gson.toJson(data);
  }

  private Object createErrorResponse(Response response, int statusCode, String message) {
    response.status(statusCode);
    response.type("application/json");
    return gson.toJson(Map.of("message", message));
  }


  private Object handleRegistrationError(Response response, UserData user, RuntimeException e) {
    if (user.password() == null || user.email() == null) {
      return createErrorResponse(response, HTTP_BAD_REQUEST, e.getMessage());
    } else {
      return createErrorResponse(response, HTTP_FORBIDDEN, e.getMessage());
    }
  }

  public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
