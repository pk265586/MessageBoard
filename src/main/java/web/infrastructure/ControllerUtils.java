package web.infrastructure;

import com.sun.net.httpserver.HttpServer;

/**
 * Class implementing static controller utilities.
 */
public class ControllerUtils {
    /**
     * Registers controller for http server.
     * @param server HttpServer instance to Register controller for.
     * @param controller Controller instance to register.
     */
    public static void registerController(HttpServer server, Controller controller) {
        controller.registerEndpoints(server);
    }
}
