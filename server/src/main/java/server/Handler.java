package server;

import com.google.gson.Gson;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class Handler {
  private final UserService userService;

  public Handler(UserService userService){
    this.userService=userService;
  }


  public static Object registerRequest(Request request, Response response) {
    var user  = deserialize(request);
    user = UserService.register(user);
    return new Gson().toJson(user);

  }


  private static Object deserialize(Request request){
    return new Gson().fromJson(request.body(), UserData.class);
  }
}
