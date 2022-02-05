package dataLayer;

import model.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserRepository {
    private static final String INSERT_TEXT = "Insert Into User (Name) Values (?)";
    private static final String UPDATE_TEXT = "Update User Set Name = ? Where Id = ?";

    private static final String FIND_NAME_TEXT = "Select * From User Where Name = ?";
    private static final String FIND_ID_TEXT = "Select * From User Where Id = ?";

    private String connectionString;
    public UserRepository(String connectionString){
        this.connectionString = connectionString;
    }

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

    public UserModel getUserByName(String name){
        var helper = new SqlHelper(connectionString);
        return helper.getEntity(FIND_NAME_TEXT, name, this::getUserByResultSet);
    }

    public UserModel getUserById(int id){
        var helper = new SqlHelper(connectionString);
        return helper.getEntity(FIND_ID_TEXT, id, this::getUserByResultSet);
    }

    private UserModel getUserByResultSet(ResultSet set) {
        try {
            return new UserModel(set.getInt("Id"), set.getString("Name"));
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
