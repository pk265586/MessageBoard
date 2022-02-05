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
class VoteServiceTest {
    @Test
    void addVote() {
        var userService = new UserService();
        var user = new UserModel("TestGetTopMessages1");
        userService.registerUser(user);

        var messageService = new MessageService();
        var message = new MessageModel(user.getId(), new Date(), "Message 1");
        messageService.insertMessage(message);

        var voteService = new VoteService();
        var voteResult = voteService.AddVote(new VoteModel(user.getId(), message.getId(), 1));
        assertTrue(voteResult.isSuccess() &&
                messageService.getMessageById( message.getId()).getVotes() == 1);

        var badVoteResult = voteService.AddVote(new VoteModel(user.getId(), message.getId(), 1));
        assertTrue(badVoteResult.isError() &&
                messageService.getMessageById( message.getId()).getVotes() == 1);
    }
}