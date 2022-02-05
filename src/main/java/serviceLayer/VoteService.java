package serviceLayer;

import dataLayer.MessageRepository;
import dataLayer.UserRepository;
import dataLayer.VoteRepository;
import model.VoteModel;
import utils.AppSettings;
import utils.OperationResult;

/**
 * Service class implementing vote operations (as of today, it's only one operation) required by business logic
 */
public class VoteService {
    /**
     * Inserts new vote into DB; checks that user and message exist. If the operation succeeds, updates vote count in message record.
     * @param vote Vote model to be stored in DB.
     * @return OperationResult object telling whether the operation had success or failure.
     */
    public OperationResult AddVote(VoteModel vote) {
        if (!userExists(vote.getUserId()))
            return OperationResult.Failure("User " + vote.getUserId() + " does not exist!");

        var messageRepository = new MessageRepository(AppSettings.getInstance().getConnectionString());
        var message = messageRepository.getMessageById(vote.getMessageId());

        if (message == null)
            return OperationResult.Failure("Message " + vote.getMessageId() + " does not exist!");

        var voteRepository = new VoteRepository(AppSettings.getInstance().getConnectionString());
        if (voteRepository.isVoteExists(vote.getUserId(), vote.getMessageId()))
            return OperationResult.Failure("User already voted for the message!");

        voteRepository.saveVote(vote);
        messageRepository.updateMessageVote(vote.getMessageId(), vote.getVoteValue());

        return OperationResult.Success();
    }

    private boolean userExists(int userId) {
        var userRepository = new UserRepository(AppSettings.getInstance().getConnectionString());
        return userRepository.getUserById(userId) != null;
    }
}
