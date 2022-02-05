package serviceLayer;

import dataLayer.UserRepository;
import model.UserModel;
import utils.AppSettings;
import utils.OperationResult;

/**
 * Service class implementing user operations required by business logic
 */
public class UserService {
    /**
     * Inserts new user into DB, prevents duplicate usernames. If the operation succeeds, id of newly created user is stored back to the model.
     * @param user User model to be inserted into DB.
     * @return OperationResult object telling whether the operation had success or failure.
     */
    public OperationResult registerUser(UserModel user) {
        var userRepository = getUserRepository();
        var existingUser = userRepository.getUserByName(user.getName());
        if (existingUser != null)
            return OperationResult.Failure("User " + user.getName() + " already exists");

        userRepository.saveUser(user);
        return OperationResult.Success();
    }

    /**
     * Returns user by his name, or null, if no user is found.
     * @param userName Name of the user to find.
     * @return User model from DB, or null, if no user is found.
     */
    public UserModel getUserByName(String userName) {
        var userRepository = getUserRepository();
        return userRepository.getUserByName(userName);
    }

    /**
     * Returns user by his id, or null, if no user is found.
     * @param userId Id of the user to find.
     * @return User model from DB, or null, if no user is found.
     */
    public UserModel getUserById(int userId) {
        var userRepository = getUserRepository();
        return userRepository.getUserById(userId);
    }

    private UserRepository getUserRepository() {
        return new UserRepository(AppSettings.getInstance().getConnectionString());
    }
}
