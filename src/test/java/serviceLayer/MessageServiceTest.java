package serviceLayer;

import model.MessageModel;
import model.UserModel;
import model.VoteModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testUtils.BeforeAllTests;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BeforeAllTests.class)
class MessageServiceTest {
    @Test
    void insertMessage() {
        var userService = new UserService();
        var user = new UserModel("TestInsertMessageUser");
        userService.registerUser(user);

        var messageService = new MessageService();
        var message = new MessageModel(user.getId(), new Date(), "New message text");
        var result = messageService.insertMessage(message);
        assertTrue(result.isSuccess() && message.getId() > 0);

        var badMessage = new MessageModel(-1, new Date(), "Bad message text");
        var badResult = messageService.insertMessage(badMessage);
        assertTrue(badResult.isError());
    }

    @Test
    void updateMessageText() {
        var userService = new UserService();
        var user = new UserModel("TestEditMessageUser");
        userService.registerUser(user);

        var messageService = new MessageService();
        var message = new MessageModel(user.getId(), new Date(), "New message text");
        messageService.insertMessage(message);

        final String updatedText = "Updated message text";
        var result = messageService.updateMessageText(message.getId(), updatedText);
        var checkMessage = messageService.getMessageById(message.getId());
        assertTrue(result.isSuccess() && checkMessage.getText().equals(updatedText));

        var badResult = messageService.updateMessageText(-1, updatedText);
        assertTrue(badResult.isError());
    }

    @Test
    void getMessageById() {
        var userService = new UserService();
        var user = new UserModel("TestGetMessageByIdUser");
        userService.registerUser(user);

        var messageService = new MessageService();
        var message = new MessageModel(user.getId(), new Date(), "New message text");
        messageService.insertMessage(message);

        var checkMessage = messageService.getMessageById(message.getId());
        assertTrue(checkMessage != null &&
                checkMessage.getId() == message.getId() &&
                checkMessage.getUserId() == message.getUserId() &&
                checkMessage.getDate().equals(message.getDate()) &&
                checkMessage.getText().equals(message.getText()));
    }

    @Test
    void getTopMessages() {
        var userService = new UserService();
        var user1 = new UserModel("TestGetTopMessages1");
        userService.registerUser(user1);
        var user2 = new UserModel("TestGetTopMessages2");
        userService.registerUser(user2);

        var messageService = new MessageService();
        var message1 = new MessageModel(user1.getId(), new Date(), "Message 1");
        var message2 = new MessageModel(user1.getId(), new Date(), "Message 2");
        var message3 = new MessageModel(user1.getId(), new Date(), "Message 3");
        var message4 = new MessageModel(user1.getId(), new Date(), "Message 4");
        var message5 = new MessageModel(user1.getId(), new Date(), "Message 5");
        messageService.insertMessage(message1);
        messageService.insertMessage(message2);
        messageService.insertMessage(message3);
        messageService.insertMessage(message4);
        messageService.insertMessage(message5);

        var voteService = new VoteService();
        // 2 votes for 1st message
        voteService.AddVote(new VoteModel(user1.getId(), message1.getId(), 1));
        voteService.AddVote(new VoteModel(user2.getId(), message1.getId(), 1));

        // -1 vote for 2nd message
        voteService.AddVote(new VoteModel(user1.getId(), message2.getId(), -1));

        // 1 vote for 3rd message
        voteService.AddVote(new VoteModel(user1.getId(), message3.getId(), 1));

        // -1 vote for 4th message
        voteService.AddVote(new VoteModel(user1.getId(), message4.getId(), -1));

        // take 2 messages - we expect to get 1st and 3rd
        var topMessages = messageService.getTopMessages(2);
        assertTrue(topMessages != null &&
                topMessages.size() == 2 &&
                topMessages.get(0).getId() == message1.getId() &&
                topMessages.get(1).getId() == message3.getId());
    }
}