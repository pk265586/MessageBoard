package web.infrastructure;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.OperationResult;
import utils.Func2Args;
import utils.MathUtils;
import web.model.ResponseEntity;

/**
 * Base class for Web controllers used in this application.
 */
public abstract class Controller {
    private final ObjectMapper objectMapper;

    public Controller(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * When overridden in class descendants, this method should register all endpoints handled by controller.
     * @param server HttpServer instance to register endpoints for.
     */
    public abstract void registerEndpoints(HttpServer server);

    /**
     * Registers handler for endpoint with "POST" method.
     * @param server HttpServer instance to register endpoint for.
     * @param address Uri path to associate with the handler.
     * @param inputClass Class of input object.
     * @param handler Handler method for the endpoint.
     * @param <TInput> Type of input object.
     * @param <TOutput> Type of output object.
     */
    protected <TInput, TOutput> void registerPostEndpoint(HttpServer server, String address, Class<TInput> inputClass, Function<TInput, ResponseEntity<TOutput>> handler) {
        registerPostEndpoint(server, address, inputClass, (TInput input, Headers headers) -> handler.apply(input));
    }

    /**
     * Registers handler for endpoint with "POST" method.
     * @param server HttpServer instance to register endpoint for.
     * @param address Uri path to associate with the handler.
     * @param inputClass Class of input object.
     * @param handler Handler method for the endpoint.
     * @param <TInput> Type of input object.
     * @param <TOutput> Type of output object.
     */
    protected <TInput, TOutput> void registerPostEndpoint(HttpServer server, String address, Class<TInput> inputClass, Func2Args<TInput, Headers, ResponseEntity<TOutput>> handler) {
        server.createContext(address, (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                var requestHeaders = exchange.getRequestHeaders();

                var stream = exchange.getRequestBody();

                TInput input = objectMapper.readValue(stream, inputClass);
                var output = handler.apply(input, requestHeaders);

                writeOutput(exchange, output);
            }
        }));
    }

    /**
     * Registers handler for endpoint with "GET" method.
     * @param server HttpServer instance to register endpoint for.
     * @param address Uri path to associate with the handler.
     * @param handler Handler method for the endpoint.
     * @param <TOutput> Type of output object.
     */
    protected <TOutput> void registerGetEndpoint(HttpServer server, String address, Function<Headers, ResponseEntity<TOutput>> handler) {
        server.createContext(address, (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                var requestHeaders = exchange.getRequestHeaders();

                var output = handler.apply(requestHeaders);

                writeOutput(exchange, output);
            }
        }));
    }

    private <TOutput> void writeOutput(com.sun.net.httpserver.HttpExchange exchange, ResponseEntity<TOutput> output) throws IOException {
        exchange.getResponseHeaders().putAll(WebConst.getJsonContentHeaders());
        exchange.sendResponseHeaders(output.getStatusCode(), 0);

        OutputStream outStream = exchange.getResponseBody();
        if (output.isError())
            objectMapper.writeValue(outStream, output.getErrorMessage());
        else
            objectMapper.writeValue(outStream, output.getBody());
        outStream.flush();
        outStream.close();
        exchange.close();
    }

    /**
     * Helper method to get user id header value of a request (by local conventions).
     * @param headers Headers object of the request.
     * @return user id value, or 0 if header either not found, or holds non-integer value.
     */
    protected int getUserIdHeaderValue(Headers headers) {
        String strResult = headers.getFirst(WebConst.USER_ID_HEADER);
        return MathUtils.tryParseInt(strResult);
    }

    /**
     * Creates response entity with standard error message when authentication failed
     */
    protected ResponseEntity<String> createAuthenticationError() {
        return new ResponseEntity<>("", ResponseStatus.BAD_REQUEST, "Authentication required");
    }

    /**
     * Creates response entity depending on the passed operation result.
     * @param result OperationResult object denoting either success or failure of some operation.
     * @return Either response entity with OK status and empty body is case of successful operation, or entity with "bad request" status and error message in case of failed operation.
     */
    protected ResponseEntity<String> createResponseByOperationResult(OperationResult result) {
        return result.isSuccess() ? new ResponseEntity<>("") : new ResponseEntity<>("", ResponseStatus.BAD_REQUEST, result.getErrorMessage());
    }
}
