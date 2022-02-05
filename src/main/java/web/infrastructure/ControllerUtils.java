package web.infrastructure;

import com.sun.net.httpserver.HttpServer;

public class ControllerUtils {
    public static void registerController(HttpServer server, Controller controller){
        controller.registerEndpoints(server);
    }
}
