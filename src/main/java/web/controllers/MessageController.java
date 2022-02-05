package web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import model.MessageModel;
import model.VoteModel;
import serviceLayer.MessageService;
import serviceLayer.VoteService;
import web.infrastructure.Controller;
import web.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageController extends Controller {
    public MessageController(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void registerEndpoints(HttpServer server) {
        registerPostEndpoint(server, "/api/messages/post", PostMessageRequest.class, this::postMessage);
        registerPostEndpoint(server, "/api/messages/edit", EditMessageRequest.class, this::editMessage);
        registerPostEndpoint(server, "/api/messages/vote", VoteRequest.class, this::vote);
        registerPostEndpoint(server, "/api/messages/top-messages", TopMessagesRequest.class, this::getTopMessages);
    }

    private ResponseEntity<String> postMessage(PostMessageRequest messageRequest, Headers headers) {
        int userId = getUserIdHeaderValue(headers);
        if (userId <= 0)
            return createAuthenticationError();

        var message = new MessageModel(userId, new Date(), messageRequest.getText());
        var service = new MessageService();
        var result = service.insertMessage(message);
        return createResponseByOperationResult(result);
    }

    private ResponseEntity<String> editMessage(EditMessageRequest messageRequest, Headers headers) {
        int userId = getUserIdHeaderValue(headers);
        if (userId <= 0)
            return createAuthenticationError();

        var service = new MessageService();
        var result = service.updateMessageText(messageRequest.getId(), messageRequest.getText());
        return createResponseByOperationResult(result);
    }

    private ResponseEntity<String> vote(VoteRequest voteRequest, Headers headers) {
        int userId = getUserIdHeaderValue(headers);
        if (userId <= 0)
            return createAuthenticationError();

        var service = new VoteService();
        int voteValue = voteRequest.isPlus() ? 1 : -1;
        var result = service.AddVote(new VoteModel(userId, voteRequest.getId(), voteValue));
        return createResponseByOperationResult(result);
    }

    private ResponseEntity<MessagesResponseItem[]> getTopMessages(TopMessagesRequest messagesRequest) {
        var service = new MessageService();
        ArrayList<MessageModel> result = service.getTopMessages(messagesRequest.getCount());

        var dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        MessagesResponseItem[] responseItems = result.stream()
                .map(x -> new MessagesResponseItem(x.getUserName(), dateFormat.format(x.getDate()), x.getText(), x.getVotes()))
                .toArray(MessagesResponseItem[]::new);

        return new ResponseEntity<>(responseItems);
    }
}
