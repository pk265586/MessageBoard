package dataLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Function;

public class SqlHelper {
    private String connectionString;

    public SqlHelper(String connectionString) {
        this.connectionString = connectionString;
    }

    public void testSql() {
        try (var conn = DriverManager.getConnection(connectionString)) {
            System.out.println("Connection to SQLite has been established.");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select * From User");

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("Id") + "\t" +
                        rs.getString("Name"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public <T> T getEntity(String sqlText, Function<ResultSet, T> mapper) {
        return getEntity(sqlText, null, mapper);
    }

    public <T> T getEntity(String sqlText, Object parameter, Function<ResultSet, T> mapper) {
        return getEntity(sqlText, new Object[] {parameter}, mapper);
    }

    public <T> T getEntity(String sqlText, Object[] parameters, Function<ResultSet, T> mapper) {
        var list = getEntityList(sqlText, parameters, mapper);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    public <T> ArrayList<T> getEntityList(String sqlText, Function<ResultSet, T> mapper) {
        return getEntityList(sqlText, null, mapper);
    }

    public <T> ArrayList<T> getEntityList(String sqlText, Object parameter, Function<ResultSet, T> mapper) {
        return getEntityList(sqlText, new Object[] {parameter}, mapper);
    }

    public <T> ArrayList<T> getEntityList(String sqlText, Object[] parameters, Function<ResultSet, T> mapper) {
        try (var connection = DriverManager.getConnection(connectionString)) {
            var statement = connection.prepareStatement(sqlText);
            initPreparedStatement(statement, parameters);

            ResultSet set = statement.executeQuery();
            var result = new ArrayList<T>();
            while (set.next()) {
                result.add(mapper.apply(set));
            }
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean rowExists(String sqlText, Object[] parameters) {
        try (var connection = DriverManager.getConnection(connectionString)) {
            var statement = connection.prepareStatement(sqlText);
            initPreparedStatement(statement, parameters);

            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int execSql(String sqlText){
        return execSql(sqlText, null);
    }

    public int execSql(String sqlText, Object[] parameters) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            connection.setAutoCommit(false);
            var statement = connection.prepareStatement(sqlText, PreparedStatement.RETURN_GENERATED_KEYS);
            initPreparedStatement(statement, parameters);

            statement.executeUpdate();

            int newId = 0;
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newId = resultSet.getInt(1);
            }

            connection.commit();

            return newId;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            return 0;
        }
    }

    private PreparedStatement initPreparedStatement(PreparedStatement statement, Object[] parameters) throws SQLException {
        if (parameters != null) {
            for (int idx = 0; idx < parameters.length; idx++) {
                statement.setObject(idx + 1, parameters[idx]);
            }
        }
        return statement;
    }

}
