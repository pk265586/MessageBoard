package dataLayer;

import model.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for required CRUD operations on User table
 */
public class UserRepository {
    private static final String INSERT_TEXT = "Insert Into User (Name) Values (?)";
    private static final String UPDATE_TEXT = "Update User Set Name = ? Where Id = ?";

    private static final String FIND_NAME_TEXT = "Select * From User Where Name = ?";
    private static final String FIND_ID_TEXT = "Select * From User Where Id = ?";

    private final String connectionString;

    public UserRepository(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Inserts or updates user in DB. If user's id <= 0, then the operation is insert, and id of newly created user is stored back to the model. Otherwise, the operation is update.
     * @param user User model to be stored in DB.
     */
    public void saveUser(UserModel user) {
        boolean isNew = user.isNew();
        String sqlText = isNew ? INSERT_TEXT : UPDATE_TEXT;
        ArrayList<Object> parameters = new ArrayList<>(Arrays.asList(user.getName()));
        if (!isNew) {
            parameters.add(user.getId());
        }
        var newId = new SqlHelper(connectionString).execSql(sqlText, parameters.toArray());

        if (isNew) {
            user.setId(newId);
        }
    }

    /**
     * Returns user by his name, or null, if no user is found.
     * @param name Name of the user to find.
     * @return User model from DB, or null, if no user is found.
     */
    public UserModel getUserByName(String name) {
        var helper = new SqlHelper(connectionString);
        return helper.getEntity(FIND_NAME_TEXT, name, this::getUserByResultSet);
    }

    /**
     * Returns user by his id, or null, if no user is found.
     * @param id Id of the user to find.
     * @return User model from DB, or null, if no user is found.
     */
    public UserModel getUserById(int id) {
        var helper = new SqlHelper(connectionString);
        return helper.getEntity(FIND_ID_TEXT, id, this::getUserByResultSet);
    }

    private UserModel getUserByResultSet(ResultSet set) {
        try {
            return new UserModel(set.getInt("Id"), set.getString("Name"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
