import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import utils.AppSettings;
import web.controllers.*;
import web.infrastructure.ControllerUtils;
import web.infrastructure.WebConst;

public class MainWebEntry {
    public static void main(String[] args) throws Exception {
        initEnvironment();
        initWebApi();
    }

    private static void initEnvironment() {
        AppSettings.getInstance().load();
    }

    private static void initWebApi() throws Exception {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        ControllerUtils.registerController(server, new UserController(WebConst.getObjectMapper()));
        ControllerUtils.registerController(server, new MessageController(WebConst.getObjectMapper()));

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
