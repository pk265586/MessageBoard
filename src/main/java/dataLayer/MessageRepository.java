package dataLayer;

import model.MessageModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for required CRUD operations on Message table
 */
public class MessageRepository {
    private static final String INSERT_TEXT = "Insert Into Message (UserId, CreationDate, MessageText) Values (?, ?, ?)";
    private static final String UPDATE_TEXT = "Update Message Set UserId = ?, CreationDate = ?, MessageText = ? Where Id = ?";
    private static final String UPDATE_VOTE_TEXT = "Update Message Set VoteCount = VoteCount + ? Where Id = ?";
    private static final String FIND_ID_TEXT =
                    "Select Message.*, User.Name as UserName "+
                    "From Message "+
                    "Inner Join User on User.Id = Message.UserId "+
                    "Where Message.Id = ?";
    private static final String FIND_TOP_MESSAGES_TEXT =
                    "Select Message.*, User.Name as UserName "+
                    "From Message "+
                    "Inner Join User on User.Id = Message.UserId "+
                    "Order by Message.VoteCount desc, Message.CreationDate desc "+
                    "Limit ?";

    private final String connectionString;
    public MessageRepository(String connectionString){
        this.connectionString = connectionString;
    }

    /**
     * Inserts or updates message in DB. If message's id <= 0, then the operation is insert, and id of newly created message is stored back to the model. Otherwise, the operation is update.
     * @param message Message model to be stored in DB.
     */
    public void saveMessage(MessageModel message) {
        boolean isNew = message.isNew();
        String sqlText = isNew ? INSERT_TEXT : UPDATE_TEXT;
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(message.getUserId(), message.getDate(), message.getText()));

        if (!isNew) {
            parameters.add(message.getId());
        }
        var newId = new SqlHelper(connectionString).execSql(sqlText, parameters.toArray());

        if (isNew) {
            message.setId(newId);
        }
    }

    /**
     * Returns message by its id, or null, if no message is found.
     * @param id Id of the message to find.
     * @return Message model from DB, or null, if no message is found.
     */
    public MessageModel getMessageById(int id){
        var helper = new SqlHelper(connectionString);
        return helper.getEntity(FIND_ID_TEXT, id, this::getMessageByResultSet);
    }

    /**
     * Returns "top" messages, ordering them by vote count (descending), then by date (descending).
     * @param count Maximum number of messages to return.
     * @return ArrayList of top messages.
     */
    public ArrayList<MessageModel> getTopMessages(int count) {
        var helper = new SqlHelper(connectionString);
        return helper.getEntityList(FIND_TOP_MESSAGES_TEXT, count, this::getMessageByResultSet);
    }

    /**
     * Updates message's vote count. This method should be used to vote for message, to reduce concurrency risk.
     * @param messageId Id of the message.
     * @param voteDelta Change in message's amount of votes. Can use 1 to upvote, or -1 to down-vote.
     */
    public void updateMessageVote(int messageId, int voteDelta) {
        var helper = new SqlHelper(connectionString);
        helper.execSql(UPDATE_VOTE_TEXT, new Object[] { voteDelta, messageId });
    }

    private MessageModel getMessageByResultSet(ResultSet set) {
        try {
            return new MessageModel(set.getInt("Id"),
                    set.getInt("UserId"),
                    set.getDate("CreationDate"),
                    set.getString("MessageText"),
                    set.getInt("VoteCount"),
                    set.getString("UserName"));
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
