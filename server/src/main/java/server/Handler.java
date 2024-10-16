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


  public  Object registerRequest(Request request, Response response) {
    var user  = deserialize(request);
    user = userService.register((UserData) user);
    return new Gson().toJson(user);
  }


  private  Object deserialize(Request request){
    return new Gson().fromJson(request.body(), UserData.class);
  }
}
