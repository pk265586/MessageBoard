package serviceLayer;

import dataLayer.UserRepository;
import model.UserModel;
import utils.AppSettings;
import utils.OperationResult;

public class UserService {
    public OperationResult registerUser(UserModel user) {
        var userRepository = getUserRepository();
        var existingUser = userRepository.getUserByName(user.getName());
        if (existingUser != null)
            return OperationResult.Failure("User " + user.getName() + " already exists");

        userRepository.saveUser(user);
        return OperationResult.Success();
    }

    public UserModel getUserByName(String userName) {
        var userRepository = getUserRepository();
        return userRepository.getUserByName(userName);
    }

    public UserModel getUserById(int userId) {
        var userRepository = getUserRepository();
        return userRepository.getUserById(userId);
    }

    private UserRepository getUserRepository() {
        return new UserRepository(AppSettings.getInstance().getConnectionString());
    }
}
