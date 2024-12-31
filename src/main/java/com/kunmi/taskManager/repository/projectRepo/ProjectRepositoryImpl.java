package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.service.project.Project;
import com.kunmi.taskManager.utils.db.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Logger log = LoggerFactory.getLogger(ProjectRepositoryImpl.class);
    private final Map<String, Map<String, Project>> projectRepo = new HashMap<>();

    @Override
    public void addProject(String userId, Project project) {
//        projectRepo
//                .computeIfAbsent(userId, k -> new HashMap<>())
//                .put(project.getId(), project);
//        log.info("Project is called and being added");

        String insertSQL = "insert into project (project_name, create_date, user_id) " +
                "values(?, ?, ?)";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, project.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(project.getCreateDate()));
            preparedStatement.setInt(3, Integer.parseInt(userId));

            preparedStatement.executeUpdate();
            log.info("Project added successfully");
        } catch (SQLException e) {
            log.error("Error adding projects: {}",  e.getMessage());
        }

    }

    @Override
    public Project getProject(String projectId, String userId) throws ProjectNotFoundException {
        //Map<String, Project> userProjects = projectRepo.get(userId);
        String selectSQL = "select * from project where project_id = ? and user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.setInt(2, Integer.parseInt(userId));

            try(ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String pID = String.valueOf(rs.getInt(1));
                    String projectName = rs.getString(2);
                    LocalDateTime createDate = rs.getTimestamp(3).toLocalDateTime();
                    String uId = String.valueOf(rs.getInt(4));

                    return new Project(pID, projectName, createDate, uId);
                }
            }

        } catch (SQLException e) {
            log.error("An error occurred while fetching project {}", e.getMessage());
            throw new RuntimeException("Error fetching project from the database" + e.getMessage());
        }

        //return (userProjects != null) ? userProjects.get(projectId) : null;
        throw new ProjectNotFoundException("Project not found with the ID: " +  projectId);

    }

    @Override
    public List<Project> getUserProjects(String userId) {
//        Map<String, Project> userProjects = projectRepo.get(userId);
//        return (userProjects != null) ? new ArrayList<>(userProjects.values()) : null;
        String selectSQL = "select * from project where user_id = ?";

        List<Project> projectList = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(userId));

            try(ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String pID = String.valueOf(rs.getInt(1));
                    String projectName = rs.getString(2);
                    LocalDateTime createDate = rs.getTimestamp(3).toLocalDateTime();
                    String uId = String.valueOf(rs.getInt(4));

                    projectList.add(new Project(pID, projectName, createDate, uId));
                }
            }

        } catch (SQLException e) {
            log.error("Error occurred while fetching project {}", e.getMessage());
            throw new RuntimeException("Error fetching project from the database" + e.getMessage());
        }

        //return (userProjects != null) ? userProjects.get(projectId) : null;
        return projectList.isEmpty() ? Collections.emptyList() : projectList;
    }

    @Override
    public void removeProject(String projectId, String userId) {
//        Map<String, Project> userProjects = projectRepo.get(userId);
//        if (userProjects != null) {
//            userProjects.remove(projectId);
//            log.info("Project removed successfully");
//
//            if (userProjects.isEmpty()) {
//                projectRepo.remove(userId);
//            }
//        }

        String deleteSQL = "delete from project where project_id = ? and user_id = ?";

        try (Connection connection = DatabaseUtil.getDataSource().getConnection();
            var preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.setInt(2, Integer.parseInt(userId));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Project with ID {} for user {} removed successfully", projectId, userId);
            } else {
                log.warn("No project found with ID {} for user {}", projectId, userId);
            }

        } catch (SQLException e) {
            log.error("An error encountered while removing the project: {}", e.getMessage());
        }
    }

    @Override
    public void removeAllProjectsForUser(String userid)
    {
        //projectRepo.remove(userid);
        //log.info("All Projects have been removed successfully");

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
//        for (Map<String, Project> userProjects : projectRepo.values()) {
//            if (userProjects.containsKey(projectId)) {
//                return true;
//            }
//        }
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
}
