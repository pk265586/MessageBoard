package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import model.UserModel;
import serviceLayer.UserService;
import web.infrastructure.Controller;
import web.model.ResponseEntity;
import web.infrastructure.ResponseStatus;
import web.model.UserLoginRequest;
import web.model.UserLoginResponse;
import web.model.UserRegistrationRequest;

public class UserController extends Controller {
    public UserController(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void registerEndpoints(HttpServer server) {
        registerPostEndpoint(server, "/api/users/register", UserRegistrationRequest.class, this::userRegistration);
        registerPostEndpoint(server, "/api/users/login", UserLoginRequest.class, this::login);
    }

    private ResponseEntity<String> userRegistration(UserRegistrationRequest request) {
        var service = new UserService();
        var user = new UserModel(request.getUsername());
        var result = service.registerUser(user);

        return createResponseByOperationResult(result);
    }

    private ResponseEntity<UserLoginResponse> login(UserLoginRequest request) {
        var service = new UserService();
        var user = service.getUserByName(request.getUsername());

        return user != null ?
                new ResponseEntity<>(new UserLoginResponse(user.getId())) :
                new ResponseEntity<>(null, ResponseStatus.BAD_REQUEST, "User " + request.getUsername() + " not found!");
    }
}
