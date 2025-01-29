package com.kunmi.taskManager.repository.projectRepo;

import com.google.gson.Gson;
import com.kunmi.taskManager.models.Project;
import com.kunmi.taskManager.utils.hibernate.HibernateUtil;
import com.kunmi.taskManager.utils.redis.RedisUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Logger log = LoggerFactory.getLogger(ProjectRepositoryImpl.class);

    @Override
    public void saveProject(Project project) {
        addProjectToDatabase(project);
        addProjectToRedis(project);
    }

    @Override
    public Optional<Project> findById(Long projectId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Project project = session.createQuery("from Project p where p.id = :projectId", Project.class)
                    .setParameter("projectId", projectId)
                    .uniqueResult();

            return Optional.ofNullable(project);
        }
    }

    @Override
    public Project getProject(Long projectId, Long userId) {
        Project project = getProjectFromRedis(projectId);

        if (project == null) {
            project = getProjectFromDataBase(projectId);

            if (project != null) {
                addProjectToRedis(project);
            }
        }
        return project;
    }

    @Override
    public List<Project> getUserProjects(Long userId) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Project p where p.user.id = :userId", Project.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (Exception e) {
            log.error("Error fetching projects - {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeProject(Long projectId, Long userId) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            int rowsAffected = session.createQuery("delete from Project p where p.id = :projectId")
                    .setParameter("projectId", projectId)
                    .executeUpdate();

            transaction.commit();

            if (rowsAffected == 0) {
                log.warn("No project found with ID: {}", projectId);
            } else {
                log.info("Project with ID: {} successfully deleted", projectId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("An error encountered while removing the project: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeAllProjectsForUser(Long userId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "delete from Project p where p.user.id = :userId";
            int result = session.createQuery(hql)
                            .setParameter("userId", userId)
                            .executeUpdate();

            transaction.commit();

            if (result == 0) {
                log.warn("No project found with the userID {}", userId);
            } else {
                log.info("{} projects deleted for user ID: {}", result, userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting all project with ID: {}", userId);
            throw e;
        }
    }

    @Override
    public boolean existsById(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID must not be null or blank");
        }

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

        String hql = "from Project p where p.id= :projectId";

        Long count = session.createQuery(hql, Project.class)
                .setParameter("projectId", projectId)
                .uniqueResult().getId();

        return count != null && count > 0;
        }
    }

    @Override
    public void updateProject(Project project) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(project);
            updateProjectInRedis(project);

            transaction.commit();
            log.info("Project updated successfully: ID = {}", project.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("An error occurred while updating project: {}", e.getMessage());
            throw e;
        }
    }


    private void addProjectToDatabase(Project project) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(project);

            transaction.commit();

            log.info("Project added to database successfully: {}", project.getName());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error adding project to database {}", e.getMessage());
            throw e;
        }
    }

    private void addProjectToRedis(Project project) {
        if (project == null || project.getId() == null) {
            log.error("Failed to save project to Redis: Project or Project ID is null");
            return;
        }

        try (Jedis jedis = RedisUtil.getJedis()) {
            String projectJson = serializeProjectToJson(project);

            if (projectJson != null) {
                jedis.set(String.valueOf(project.getId()), projectJson);
                log.info("Project cached in Redis for ID: {}", project.getId());
            } else {
                log.error("Failed to serialize project with ID: {}", project.getId());
            }
        } catch (JedisException e) {
            log.error("Failed to save project to Redis for ID: {}. Error: {}", project.getId(), e.getMessage());
        }
    }

    private String serializeProjectToJson(Project project) {
        try {
            Gson gson = new Gson();
            return gson.toJson(project);
        } catch (Exception e) {
            log.error("Error serializing project: {}", e.getMessage());
            return null;
        }
    }

    private Project getProjectFromRedis(Long projectId) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String projectJson = jedis.get(String.valueOf(projectId));
            if (projectJson != null) {
                return new Gson().fromJson(projectJson, Project.class);
            }
        } catch (JedisException e) {
            log.error("Failed to get project from Redis, falling back to database: {}", e.getMessage());
        }
        return null;
    }

    private void updateProjectInRedis(Project project) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String projectJson = jedis.get(String.valueOf(project.getId()));

            if (projectJson != null) {
                String updatedProjectJson = serializeProjectToJson(project);

                if(updatedProjectJson != null) {
                    jedis.expire(String.valueOf(project.getId()), 60);
                    log.info("Project with ID {} successfully updated in Redis cache", project.getId());
                }
            } else {
                log.warn("Project with ID {} not found in Redis cache. Adding it now.", project.getId());
                addProjectToRedis(project);
            }
        } catch (JedisException e) {
            log.error("Failed to update project in Redis: {}",  e.getMessage());
        }
    }

    private Project getProjectFromDataBase(Long projectId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "from Project p where p.id = :projectId";

            return session.createQuery(hql, Project.class)
                    .setParameter("projectId", projectId)
                    .uniqueResult();

        } catch (Exception e) {
            log.error("An error occurred while fetching project {}", e.getMessage());
            throw e;
        }
    }

}
