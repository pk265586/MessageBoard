package serviceLayer;

import dataLayer.MessageRepository;
import dataLayer.UserRepository;
import model.MessageModel;
import utils.AppSettings;
import utils.MessageCache;
import utils.OperationResult;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MessageService {
    public OperationResult insertMessage(MessageModel message){
        // verify that the user exists
        var settings = AppSettings.getInstance();
        var userRepository = new UserRepository(settings.getConnectionString());
        if (userRepository.getUserById(message.getUserId()) == null)
            return OperationResult.Failure("User "+message.getUserId()+" not found!");

        saveMessage(message);

        if (settings.getMessageCache() != null)
            settings.getMessageCache().clear();

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
        var cache = AppSettings.getInstance().getMessageCache();
        var repository = getMessageRepository();
        if (cache == null) {
            return repository.getTopMessages(count);
        } else {
            return getTopMessagesWithCache(repository, cache, count);
        }
    }

    private ArrayList<MessageModel> getTopMessagesWithCache(MessageRepository repository, MessageCache cache, int count) {
        var cachedMessages = cache.getCachedMessages();
        if (cachedMessages == null) {
            var result = repository.getTopMessages(count);
            cache.cacheResult(result);
            return result;
        } else {
            int cachedRowCount = cachedMessages.size();
            int dataRowsNeeded = count - cachedRowCount;
            if (dataRowsNeeded == 0) {
                return cachedMessages;
            } else if (dataRowsNeeded < 0){
                return cachedMessages.stream().limit(count).collect(Collectors.toCollection(ArrayList::new));
            } else {
                var messagesToAdd = repository.getTopMessages(dataRowsNeeded, cachedRowCount);
                cache.addResult(messagesToAdd);
                return cache.getCachedMessages();
            }
        }
    }

    private void saveMessage(MessageModel message) {
        var repository = getMessageRepository();
        repository.saveMessage(message);
    }

    private MessageRepository getMessageRepository() {
        return new MessageRepository(AppSettings.getInstance().getConnectionString());
    }
}
