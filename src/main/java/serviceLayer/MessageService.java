package serviceLayer;

import dataLayer.MessageRepository;
import dataLayer.UserRepository;
import model.MessageModel;
import utils.AppSettings;
import utils.OperationResult;

import java.util.ArrayList;

public class MessageService {
    public OperationResult insertMessage(MessageModel message){
        // verify that the user exists
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        if (userRepository.getUserById(message.getUserId()) == null)
            return OperationResult.Failure("User "+message.getUserId()+" not found!");

        saveMessage(message);
        return OperationResult.Success();
    }

    public OperationResult updateMessageText(int messageId, String newText){
        var message = getMessageById(messageId);
        if (message == null)
            return OperationResult.Failure("Message "+messageId+" not found!");

        message.setText(newText);
        saveMessage(message);
        return OperationResult.Success();
    }

    public MessageModel getMessageById(int id){
        var repository = getMessageRepository();
        return repository.getMessageById(id);
    }

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
