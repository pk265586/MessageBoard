package dataLayer;

import model.UserModel;
import org.junit.jupiter.api.extension.ExtendWith;
import testUtils.BeforeAllTests;
import utils.AppSettings;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BeforeAllTests.class)
class UserRepositoryTest {
    @org.junit.jupiter.api.Test
    void saveUser() {
        var repo = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel("TestAddUser");
        repo.saveUser(user);
        assertTrue(user.getId() > 0);
    }

    @org.junit.jupiter.api.Test
    void getUserByName() {
        final String userName = "TestUserByName";

        var repo = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel(userName);
        repo.saveUser(user);

        var findUser = repo.getUserByName(userName);
        assertTrue(findUser != null && findUser.getId() == user.getId() && findUser.getName().equals(user.getName()));
    }

    @org.junit.jupiter.api.Test
    void getUserById() {
        final String userName = "TestUserById";

        var repo = new UserRepository(AppSettings.getInstance().getConnectionString());
        var user = new UserModel(userName);
        repo.saveUser(user);

        var findUser = repo.getUserById(user.getId());
        assert findUser != null && findUser.getId() == user.getId() && findUser.getName().equals(user.getName());
    }
}