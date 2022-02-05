package dataLayer;

import model.VoteModel;

public class VoteRepository {
    private static final String INSERT_TEXT = "Insert Into UserVote (UserId, MessageId, VoteValue) Values (?, ?, ?)";
    private static final String FIND_BY_USER_AND_MESSAGE_TEXT = "Select Id From UserVote Where UserId = ? and MessageId = ? Limit 1";

    private String connectionString;
    public VoteRepository(String connectionString){
        this.connectionString = connectionString;
    }

    // Note: as of today, only vote insert is supported
    public void saveVote(VoteModel vote) {
        String sqlText = INSERT_TEXT;
        Object[] parameters = new Object[] {vote.getUserId(), vote.getMessageId(), vote.getVoteValue()};
        var newId = new SqlHelper(connectionString).execSql(sqlText, parameters);
        vote.setId(newId);
    }

    public boolean isVoteExists(int userId, int messageId){
        var helper = new SqlHelper(connectionString);
        return helper.rowExists(FIND_BY_USER_AND_MESSAGE_TEXT, new Object[] { userId, messageId });
    }
}
