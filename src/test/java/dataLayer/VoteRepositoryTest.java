package dataLayer;

import java.util.Date;
import org.junit.jupiter.api.Test;

import model.MessageModel;
import model.UserModel;
import model.VoteModel;
import org.junit.jupiter.api.extension.ExtendWith;
import testUtils.BeforeAllTests;
import utils.AppSettings;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BeforeAllTests.class)
class VoteRepositoryTest {
    @Test
    void saveVote() {
        var user = new UserModel("UserSaveVoteTest");
        new UserRepository(AppSettings.getInstance().getConnectionString()).saveUser(user);

        var message = new MessageModel(user.getId(), new Date(), "Message 1");
        new MessageRepository(AppSettings.getInstance().getConnectionString()).saveMessage(message);

        var repo = new VoteRepository(AppSettings.getInstance().getConnectionString());
        var vote = new VoteModel(user.getId(), message.getId(), 1);
        repo.saveVote(vote);
        assertTrue(vote.getId() > 0);
    }

    @Test
    void isVoteExists() {
        var user = new UserModel("UserVoteExistsTest");
        new UserRepository(AppSettings.getInstance().getConnectionString()).saveUser(user);

        var message = new MessageModel(user.getId(), new Date(), "Message 1");
        new MessageRepository(AppSettings.getInstance().getConnectionString()).saveMessage(message);

        var repo = new VoteRepository(AppSettings.getInstance().getConnectionString());
        var vote = new VoteModel(user.getId(), message.getId(), 1);
        repo.saveVote(vote);

        assertTrue(repo.isVoteExists(user.getId(), message.getId()));
        assertFalse(repo.isVoteExists(-1, message.getId()));
        assertFalse(repo.isVoteExists(user.getId(), -1));
        assertFalse(repo.isVoteExists(-1, -1));
    }
}