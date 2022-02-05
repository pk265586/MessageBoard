package serviceLayer;

import dataLayer.MessageRepository;
import dataLayer.UserRepository;
import dataLayer.VoteRepository;
import model.VoteModel;
import utils.AppSettings;
import utils.OperationResult;

public class VoteService {
    public OperationResult AddVote(VoteModel vote) {
        if (!userExists(vote.getUserId()))
            return OperationResult.Failure("User " + vote.getUserId() + " does not exist!");

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        var message = messageRepository.getMessageById(vote.getMessageId());

        if (message == null)
            return OperationResult.Failure("Message " + vote.getMessageId() + " does not exist!");

        var settings = AppSettings.getInstance();
        var voteRepository = new VoteRepository(settings.getConnectionString());
        if (voteRepository.isVoteExists(vote.getUserId(), vote.getMessageId()))
            return OperationResult.Failure("User already voted for the message!");

        voteRepository.saveVote(vote);
        messageRepository.updateMessageVote(vote.getMessageId(), vote.getVoteValue());

        if (settings.getMessageCache() != null)
            settings.getMessageCache().clear();

        return OperationResult.Success();
    }

    private boolean userExists(int userId) {
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        return userRepository.getUserById(userId) != null;
    }
}
