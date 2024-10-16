package server;

import service.UserService;
import spark.*;

public class Server {

        private final UserService service;

  public Server(UserService service) {
    this.service=service;
  }

  public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
      Handler handler = new Handler(service);
      Spark.post("/user", handler::registerRequest);


      //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
