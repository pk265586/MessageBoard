package dataLayer;

import model.VoteModel;

/**
 * Class for required CRUD operations on UserVote table
 */
public class VoteRepository {
    private static final String INSERT_TEXT = "Insert Into UserVote (UserId, MessageId, VoteValue) Values (?, ?, ?)";
    private static final String FIND_BY_USER_AND_MESSAGE_TEXT = "Select Id From UserVote Where UserId = ? and MessageId = ? Limit 1";

    private final String connectionString;

    public VoteRepository(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Inserts new vote into DB (note: only insert operation is supported today).
     * @param vote Vote model to be stored in DB.
     */
    public void saveVote(VoteModel vote) {
        Object[] parameters = new Object[]{vote.getUserId(), vote.getMessageId(), vote.getVoteValue()};
        var newId = new SqlHelper(connectionString).execSql(INSERT_TEXT, parameters);
        vote.setId(newId);
    }

    /**
     * Checks if user has already voted for a message.
     * @param userId User id who may have voted.
     * @param messageId Message id which may have been voted for.
     * @return True if user has voted for a message, false otherwise
     */
    public boolean isVoteExists(int userId, int messageId) {
        var helper = new SqlHelper(connectionString);
        return helper.rowExists(FIND_BY_USER_AND_MESSAGE_TEXT, new Object[]{userId, messageId});
    }
}
