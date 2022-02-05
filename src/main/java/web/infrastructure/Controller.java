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

public abstract class Controller {
    private final ObjectMapper objectMapper;

    public Controller(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract void registerEndpoints(HttpServer server);

    protected  <TInput, TOutput> void registerPostEndpoint(HttpServer server, String address, Class<TInput> inputClass, Function<TInput, ResponseEntity<TOutput>> function) {
        registerPostEndpoint(server, address, inputClass, (TInput input, Headers headers) -> function.apply(input));
    }

    protected <TInput, TOutput> void registerPostEndpoint(HttpServer server, String address, Class<TInput> inputClass, Func2Args<TInput, Headers, ResponseEntity<TOutput>> function) {
        server.createContext(address, (exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                var requestHeaders = exchange.getRequestHeaders();

                var stream = exchange.getRequestBody();

                TInput input = objectMapper.readValue(stream, inputClass);
                var output = function.apply(input, requestHeaders);

                writeOutput(exchange, output);
            }
        }));
    }

    protected <TInput, TOutput> void registerGetEndpoint(HttpServer server, String address, Class<TInput> inputClass, Function<Headers, ResponseEntity<TOutput>> function){
        server.createContext(address, (exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                var requestHeaders = exchange.getRequestHeaders();

                var output = function.apply(requestHeaders);

                writeOutput(exchange, output);
            }
        }));
    }

    private <TOutput> void writeOutput(com.sun.net.httpserver.HttpExchange exchange, ResponseEntity<TOutput> output) throws IOException {
        exchange.getResponseHeaders().putAll(WebUtils.getJsonContentHeaders());
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

    protected int getUserIdHeaderValue(Headers headers){
        String strResult = headers.getFirst(WebConst.USER_ID_HEADER);
        return MathUtils.tryParseInt(strResult);
    }

    protected ResponseEntity<String> createAuthenticationError(){
        return new ResponseEntity<>("", ResponseStatus.BAD_REQUEST, "Authentication required");
    }

    protected ResponseEntity<String> createResponseByOperationResult(OperationResult result){
        return result.isSuccess() ? new ResponseEntity<>("") : new ResponseEntity<>("", ResponseStatus.BAD_REQUEST, result.getErrorMessage());
    }
}
