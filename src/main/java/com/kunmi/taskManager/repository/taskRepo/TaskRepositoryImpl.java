package com.kunmi.taskManager.repository.taskRepo;

import com.kunmi.taskManager.exceptions.TaskNotFoundException;
import com.kunmi.taskManager.service.task.Task;
import com.kunmi.taskManager.utils.db.DatabaseUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {
    //private final Map<String, Map<String, Task>> taskRepo = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    @Override
    public void addTask(String projectId, Task task) {
//        taskRepo
//                .computeIfAbsent(projectId, k -> new HashMap<>())
//                .put(task.getId(), task);

        String insertSQL = "insert into task(task_id, task_name, create_date, project_id) values(?, ?, ?, ?)";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(task.getId()));
            preparedStatement.setString(2, task.getName());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getCreateDate()));
            preparedStatement.setInt(4, Integer.parseInt(projectId));


            preparedStatement.executeUpdate();
            log.info("Task created successfully");

        } catch (SQLException e) {
            log.error("Error occurred while adding task to database {}", e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public Task getTask(String taskId, String projectId) {
        //Map<String, Task> projectTask = taskRepo.get(projectId);

        String selectSQL = "select * from task where task_id = ? and project_id = ?";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(taskId));
            preparedStatement.setInt(2, Integer.parseInt(projectId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String tID = String.valueOf(rs.getInt(1));
                    String taskName = rs.getString(2);
                    LocalDateTime createDate = rs.getTimestamp(3).toLocalDateTime();
                    String pID = String.valueOf(rs.getInt(4));

                    return  new Task(tID, taskName, createDate, pID);
                }
            }

        } catch (SQLException e) {
            log.error("Error occurred while retrieving task from the database");
        }
        throw new TaskNotFoundException("Task not found with the ID: " + taskId);
    }

    @SneakyThrows
    @Override
    public List<Task> getProjectTasks(String projectId) {
         //Map<String, Task> projectTask = taskRepo.get(projectId);

        List<Task> taskList = new ArrayList<>();
        String selectSQL = "select * from task where project_id = ?";

        try(Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String tID = String.valueOf(rs.getInt(1));
                    String taskName = rs.getString(2);
                    LocalDateTime createDate = rs.getTimestamp(3).toLocalDateTime();
                    String pID = String.valueOf(rs.getInt(4));

                    taskList.add(new Task(tID, taskName, createDate, pID));
                }
            }

        } catch (SQLException e) {
            log.error("An error occurred while retrieving task from the database");
            throw new TaskNotFoundException("Task not found with the ID: " + projectId);
        }
         return taskList.isEmpty() ? Collections.emptyList() : taskList;
    }

    @Override
    public void removeTask(String taskId, String projectId) {
//        Map<String, Task> projectTask = taskRepo.get(projectId);
//
//        if (projectTask != null) {
//            projectTask.remove(taskId);
//            System.out.println("Task remove successfully!");
//
//            if (projectTask.isEmpty()) {
//                taskRepo.remove(projectId);
//            }
//        }

        String deleteSQL = "delete from task where task_id = ? and project_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(taskId));
            preparedStatement.setInt(2, Integer.parseInt(projectId));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Task with ID {} for user {} removed successfully", projectId, taskId);
            } else {
                log.warn("No Task found with ID {} for user {}", projectId, taskId);
            }

        } catch (SQLException e) {
            log.error("An error encountered while removing the project: {}", e.getMessage());
        }

    }

    @Override
    public void removeAllTask(String projectId) {
//       taskRepo.remove(projectId);
//       System.out.println("All tasks have been removed!");

        String deleteSQL = "delete from project where user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("{} records were removed", rowsAffected);
            } else {
                log.warn("No project found with user ID {}", projectId);
            }
        } catch (SQLException e) {
            log.error("Error occurred while deleting all project with ID: {}", projectId);
        }
    }

    @Override
    public void updateTask(Task task, String projectId) {
        String updateSQL = "update task set project_name = ?, create_date = ? where project_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, task.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(task.getCreateDate()));
            preparedStatement.setInt(3, Integer.parseInt(projectId));

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                log.info("Task updated successfully: ID = {}", projectId);
            } else {
                throw new TaskNotFoundException("No task found with ID: " + projectId);
            }
        } catch (SQLException | TaskNotFoundException e) {
            log.error("An error occurred while updating task: {}", e.getMessage());
            throw new RuntimeException("Error updating task in the database", e);
        }
    }
}
