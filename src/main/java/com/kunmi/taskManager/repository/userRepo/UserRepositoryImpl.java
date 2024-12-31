package com.kunmi.taskManager.repository.userRepo;


import com.kunmi.taskManager.service.user.User;
import com.kunmi.taskManager.utils.db.DatabaseUtil;
import com.kunmi.taskManager.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    //private final Map<String, User> userMemory = new HashMap<>();
    public User getUserFromRedis(String email) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String userData = jedis.get(email);
            if (userData != null) {
                return User.fromString(userData);
            }
        } catch (Exception e) {
            log.warn("Failed to get user from Redis, falling back to database:{}", e.getMessage());
        }
        return null;
    }

    public void saveUserToRedis(User user) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            jedis.set(user.getEmail(), user.toString());
            jedis.expire(user.getEmail(), 60);
        } catch (Exception e) {
            log.error("Failed to save user to Redis: {}", e.getMessage());
        }
    }

    @Override
    public User getUserFromDatabase(String email) {
        //return userMemory.get(email);
        String selectSQL = "select * from users where email = ?";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, email);

            try(var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String password = resultSet.getString("password");
                    String retrievedEmail = resultSet.getString("email");
                    String retrievedUserId = String.valueOf(resultSet.getInt("id"));

                    return new User(retrievedUserId, firstName, lastName, password, retrievedEmail);
                }
            }

        } catch (SQLException e) {
            log.error("Error retrieving record: {}",  e.getMessage());
        }
        return null;
    }

    @Override
    public void saveUserToDatabase(User user) {
        //userMemory.put(user.getEmail(), user);
        String insertSQL = "insert into users (firstName, lastName, password, email) " +
                "values(?, ?, ?, ?)";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());

            preparedStatement.execute();
            log.info("record created successfully");

        } catch (SQLException e) {
            log.error("Error creating record: {}", e.getMessage());
        }
    }

    public void saveUser(User user) {
        saveUserToDatabase(user);
        //saveUserToRedis(user);
    }

    public User getUser(String email) {
        User user = getUserFromRedis(email);

        if (user == null) {
            user = getUserFromDatabase(email);
            if (user != null) {
                saveUserToRedis(user);
            }
        }
        return user;
    }

    @Override
    public boolean userExists(String email) {
        //return userMemory.containsKey(email);
        String selectSql = "select email from users where email = ?";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(selectSql)) {

            preparedStatement.setString(1, email);

            try(var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            log.error("Error fetching record: {}", e.getMessage());
        }

        return false;
    }

}
