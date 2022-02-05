package serviceLayer;

import dataLayer.MessageRepository;
import dataLayer.UserRepository;
import model.MessageModel;
import utils.AppSettings;
import utils.OperationResult;

import java.util.ArrayList;

/**
 * Service class implementing message operations required by business logic
 */
public class MessageService {
    /**
     * Inserts new message to DB. Verifies that user that posted the message exists in DB.
     * @param message Message model to insert.
     * @return OperationResult object telling whether the operation had success or failure.
     */
    public OperationResult insertMessage(MessageModel message) {
        // verify that the user exists
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        if (userRepository.getUserById(message.getUserId()) == null)
            return OperationResult.Failure("User " + message.getUserId() + " not found!");

        saveMessage(message);
        return OperationResult.Success();
    }

    /**
     * Updates existing message's text in DB. Verifies that the message exists in DB.
     * @param messageId Message id to edit.
     * @param newText New message text.
     * @return OperationResult object telling whether the operation had success or failure.
     */
    public OperationResult updateMessageText(int messageId, String newText) {
        var message = getMessageById(messageId);
        if (message == null)
            return OperationResult.Failure("Message " + messageId + " not found!");

        message.setText(newText);
        saveMessage(message);
        return OperationResult.Success();
    }

    /**
     * Returns message by its id, or null, if no message is found.
     * @param id Id of the message to find.
     * @return Message model from DB, or null, if no message is found.
     */
    public MessageModel getMessageById(int id) {
        var repository = getMessageRepository();
        return repository.getMessageById(id);
    }

    /**
     * Returns "top" messages, ordering them by vote count (descending), then by date (descending).
     * @param count Maximum number of messages to return.
     * @return ArrayList of top messages.
     */
    public ArrayList<MessageModel> getTopMessages(int count) {
        var repository = getMessageRepository();
        return repository.getTopMessages(count);
    }

    private void saveMessage(MessageModel message) {
        var repository = getMessageRepository();
        repository.saveMessage(message);
    }

    private MessageRepository getMessageRepository() {
        return new MessageRepository(AppSettings.getInstance().getConnectionString());
    }
}
