package com.kunmi.taskManager.repository.userRepo;


import com.kunmi.taskManager.DatabaseUtil.DatabaseUtil;
import com.kunmi.taskManager.service.user.User;
import lombok.var;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRepositoryImpl implements IUserRepository {

    //private final Map<String, User> userMemory = new HashMap<>();

    @Override
    public void saveUser(User user) {
        //userMemory.put(user.getEmail(), user);
        String insertSql = "insert into users (firstName, lastName, password, email) " +
                "values(?, ?, ?, ?)";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(insertSql)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());

            preparedStatement.execute();
            System.out.println("Records created successfully!");
        } catch(SQLException e) {
            System.err.println("Error creating record: " + e.getMessage());
        }
    }

    @Override
    public boolean userExists(String email) {
        String selectSql = "select email from users where email = ?";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(selectSql)) {
            preparedStatement.setString(1, email);

                try(var resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
        } catch (SQLException e) {
            System.err.println("Error fetching record: " + e.getMessage());
        }

        return false;
    }

    @Override
    public User findUserByEmail(String email) {
        String selectSQL = "select * from users where email = ?";
        try (Connection connection = DatabaseUtil.getDataSource().getConnection()) {
            var preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);

            try(var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    //String id = String.valueOf(resultSet.getInt(1));
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String password = resultSet.getString("password");
                    String retrievedEmail = resultSet.getString("email");

                    return new User(firstName,lastName, password, retrievedEmail);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving record: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
