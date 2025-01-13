package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.service.project.Project;
import com.kunmi.taskManager.utils.db.DatabaseUtil;
import com.kunmi.taskManager.utils.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Logger log = LoggerFactory.getLogger(ProjectRepositoryImpl.class);

    @Override
    public void saveProject(String userId, Project project) {
        addProjectToDatabase(userId, project);
        addProjectToRedis(project);
    }

    @Override
    public Project getProject(String projectId, String userId) {
        Project project = getProjectFromRedis(projectId);

        if (project == null) {
            project = getProjectFromDataBase(projectId, userId);

            if (project != null) {
                addProjectToRedis(project);
            }
        }
        return project;
    }

    @Override
    public List<Project> getUserProjects(String userId) {

        List<Project> projects = new ArrayList<>();
        String selectSQL = "select * from project where user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(userId));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                projects.add(convertResultSetToProject(rs));
            }

        } catch (SQLException e) {
            log.error("Error occurred while fetching project {}", e.getMessage());
            throw new RuntimeException("Error fetching project from the database" + e.getMessage());
        }
        return projects;
    }

    private Project getProjectFromDataBase(String projectId, String userId) {

        String selectSQL = "select * from project where project_id = ? and user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.setInt(2, Integer.parseInt(userId));

            try(ResultSet rs = preparedStatement.executeQuery()) {
                convertResultSetToProject(rs);
            }

        } catch (SQLException e) {
            log.error("An error occurred while fetching project {}", e.getMessage());
            throw new RuntimeException("Error fetching project from the database" + e.getMessage());
        }
        return  null;
    }

    private Project convertResultSetToProject(ResultSet rs) throws SQLException {
        String pID = String.valueOf(rs.getInt("project_id"));
        String projectName = rs.getString("project_name");
        LocalDateTime createDate = rs.getTimestamp("create_date").toLocalDateTime();
        String uId = String.valueOf(rs.getInt("user_id"));

        return new Project(pID, projectName, createDate, uId);
    }

    @Override
    public void removeProject(String projectId, String userId) {

        String deleteSQL = "delete from project where project_id = ? and user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.setInt(2, Integer.parseInt(userId));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                removeProjectFromRedis(projectId);
                log.info("Project with ID {} for user {} removed successfully", projectId, userId);
            } else {
                log.warn("No project found with ID {} for user {}", projectId, userId);
            }

        } catch (SQLException e) {
            log.error("An error encountered while removing the project: {}", e.getMessage());
        }
    }

    @Override
    public void removeAllProjectsForUser(String userid) {
        String deleteSQL = "delete from project where user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(userid));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("{} records were removed", rowsAffected);
            } else {
                log.warn("No project found with user ID {}", userid);
            }
        } catch (SQLException e) {
            log.error("Error occurred while deleting all project with ID: {}", userid);
        }
    }

    @Override
    public boolean existsById(String projectId) {
        if (projectId == null || projectId.isBlank()) {
            throw new IllegalArgumentException("Project ID must not be null or blank");
        }

        String selectSQL = "select 1 from project where project_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));

            try(ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            log.error("error occurred while fetching project {}", e.getMessage());
        }
        return false;
    }

    @Override
    public void updateProject(String userId, Project project) {

        String updateSQL = "update project set project_name = ?, create_date = ? where project_id = ? and user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, project.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(project.getCreateDate()));
            preparedStatement.setInt(3, Integer.parseInt(project.getId()));
            preparedStatement.setInt(4, Integer.parseInt(userId));

            int affectedRows = preparedStatement.executeUpdate();

            updateProjectInRedis(project);

            if (affectedRows > 0) {
                log.info("Project updated successfully: ID = {}", project.getId());

            } else {
                throw new ProjectNotFoundException("No project found with ID: " + project.getId());
            }
        } catch (SQLException | ProjectNotFoundException e) {
            log.error("An error occurred while updating project: {}", e.getMessage());
            throw new RuntimeException("Error updating project in the database", e);
        }
    }

    private void addProjectToDatabase(String userId, Project project) {

        String insertSQL = "insert into project (project_name, create_date, user_id) " +
                "values(?, ?, ?)";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, project.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(project.getCreateDate()));
            preparedStatement.setInt(3, Integer.parseInt(userId));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        String generatedId = String.valueOf(generatedKeys.getInt(1));
                        project.setId(generatedId);
                        log.info("Project added successfully with ID: {}", generatedId);
                    }
                }
            }

        } catch (SQLException e) {
            log.error("Error adding projects: {}",  e.getMessage());
        }
    }

    private void addProjectToRedis(Project project) {
        if (project == null || project.getId() == null) {
            log.error("Failed to save project to Redis: Project or Project ID is null");
            return;
        }

        try (Jedis jedis = RedisUtil.getJedis()) {
            jedis.set(project.getId(), project.toString());
            log.info("Project cached in Redis for ID: {}", project.getId());
        } catch (JedisException e) {
            log.error("Failed to save project to Redis for ID: {}. Error: {}", project.getId(), e.getMessage());
        }
    }

    private Project getProjectFromRedis(String projectId) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String projectData = jedis.get(projectId);
            if (projectData != null) {
                return Project.fromString(projectData);
            }
        } catch (JedisException e) {
            log.error("Failed to get project from Redis, falling back to database: {}", e.getMessage());
        }
        return null;
    }

    private void updateProjectInRedis(Project project) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            if (jedis.exists(project.getId())) {
                jedis.set(project.getId(), project.toString());
                jedis.expire(project.getId(), 60);
                log.info("Project with ID {} successfully updated in Redis cache", project.getId());
            } else {
                log.warn("Project with ID {} not found in Redis cache. Adding it now.", project.getId());
                addProjectToRedis(project);
            }
        } catch (JedisException e) {
            log.error("Failed to update project in Redis: {}",  e.getMessage());
        }
    }

    private void removeProjectFromRedis(String projectId) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            Long result = jedis.del(projectId);

            if (result != null) {
                log.info("Project with ID {} removed successfully fromRedis Cache", projectId);
            } else {
                log.warn("Project with ID {} not found in Redis cache", projectId);
            }
        } catch (JedisException e) {
            log.error("Failed to remove project from Redis: {}", e.getMessage());
        }
    }


}
