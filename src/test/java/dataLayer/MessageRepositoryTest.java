package dataLayer;

import model.MessageModel;
import model.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testUtils.BeforeAllTests;
import utils.AppSettings;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BeforeAllTests.class)
class MessageRepositoryTest {
    @Test
    void saveNewMessage() {
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel("TestSaveNewMessageUser");
        userRepository.saveUser(user);

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        var message = new MessageModel(user.getId(), new Date(), "New message text");
        messageRepository.saveMessage(message);
        assertTrue(message.getId() > 0);
    }

    @Test
    void getMessageById() {
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel("TestGetMessageByIdUser");
        userRepository.saveUser(user);

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        final Date messageDate = new Date();
        final String messageText = "Another message text";
        var message = new MessageModel(user.getId(), messageDate, messageText);
        messageRepository.saveMessage(message);

        var foundMessage = messageRepository.getMessageById(message.getId());
        assertTrue(foundMessage != null &&
                foundMessage.getId() == message.getId() &&
                foundMessage.getUserId() == user.getId() &&
                foundMessage.getDate().equals(messageDate) &&
                foundMessage.getText().equals(messageText) &&
                foundMessage.getVotes() == 0);
    }

    @Test
    void saveExistingMessage() {
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel("TestSaveExistingMessageUser");
        userRepository.saveUser(user);

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        var initialMessage = new MessageModel(user.getId(), new Date(), "New message text");
        messageRepository.saveMessage(initialMessage);

        int messageId = initialMessage.getId();

        var messageToEdit = messageRepository.getMessageById(messageId);
        final String editedText = "Edited message text";
        messageToEdit.setText(editedText);
        messageRepository.saveMessage(messageToEdit);

        var checkMessage = messageRepository.getMessageById(messageId);
        assertTrue(checkMessage != null &&
                checkMessage.getId() == messageId &&
                checkMessage.getUserId() == user.getId() &&
                checkMessage.getDate().equals(initialMessage.getDate()) &&
                checkMessage.getText().equals(editedText));
    }

    @Test
    void getTopMessages() {
        // for now, simple check, just verify we get all at least all entered messages
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel("TestGetTopMessagesUser");
        userRepository.saveUser(user);

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        messageRepository.saveMessage(new MessageModel(user.getId(), new Date(), "Message 1"));
        messageRepository.saveMessage(new MessageModel(user.getId(), new Date(), "Message 2"));
        messageRepository.saveMessage(new MessageModel(user.getId(), new Date(), "Message 3"));

        var list = messageRepository.getTopMessages(3);
        assertTrue(list != null && list.size() == 3);
    }

    @Test
    void updateMessageVote() {
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel("TestUpdateMessageVoteUser");
        userRepository.saveUser(user);

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        var message = new MessageModel(user.getId(), new Date(), "New message text");
        messageRepository.saveMessage(message);

        messageRepository.updateMessageVote(message.getId(), 1);
        var checkMessage = messageRepository.getMessageById(message.getId());
        assertEquals(1, checkMessage.getVotes());
    }
}