package serviceLayer;

import dataLayer.UserRepository;
import model.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testUtils.BeforeAllTests;
import utils.AppSettings;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BeforeAllTests.class)
class UserServiceTest {
    @Test
    void registerUser() {
        final String userName = "RegisterUserTest";
        var user = new UserModel(userName);
        var service = new UserService();
        var result = service.registerUser(user);
        assertTrue(result.isSuccess() && user.getId() > 0);

        var duplicateNameUser = new UserModel(userName);
        var duplicationResult = service.registerUser(duplicateNameUser);
        assertTrue(duplicationResult.isError());
    }

    @Test
    void getUserByName() {
        final String userName = "GetUserByNameTest";

        var service = new UserService();
        var user = new UserModel(userName);
        service.registerUser(user);

        var findUser = service.getUserByName(userName);
        assertTrue(findUser != null && findUser.getId() == user.getId() && findUser.getName().equals(userName));
    }

    @Test
    void getUserById() {
        final String userName = "GetUserByIdTest";

        var service = new UserService();
        var user = new UserModel(userName);
        service.registerUser(user);

        var findUser = service.getUserById(user.getId());
        assert findUser != null && findUser.getId() == user.getId() && findUser.getName().equals(userName);
    }
}