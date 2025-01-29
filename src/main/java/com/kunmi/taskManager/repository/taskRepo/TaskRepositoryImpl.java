package com.kunmi.taskManager.repository.taskRepo;

import com.kunmi.taskManager.models.Task;
import com.kunmi.taskManager.utils.hibernate.HibernateUtil;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    @Override
    public void addTask(Task task) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            log.info("Task created successfully with ID: {}", task.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error occurred while adding task to database {}", e.getMessage());
            throw new RuntimeException("Failed to add task", e);
        }
    }

    @SneakyThrows
    @Override
    public Optional<Task> getTask(Long taskId, Long projectId) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "from Task t where t.id = :taskId and t.project.id = :projectId";
            Task task = session.createQuery(hql, Task.class)
                    .setParameter("taskId", taskId)
                    .setParameter("projectId", projectId)
                    .uniqueResult();
            return Optional.ofNullable(task);

        } catch (Exception e) {
            log.error("Error occurred while retrieving task with ID {} for project ID {}: {}",
                    taskId, projectId, e.getMessage());
            throw new RuntimeException("Failed to retrieve task", e);
        }
    }

    @SneakyThrows
    @Override
    public List<Task> getProjectTasks(Long projectId) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Task> tasks = session.createQuery("from Task t where t.project.id = :projectId", Task.class)
                    .setParameter("projectId", projectId)
                    .list();

            log.info("Retrieved {} tasks for project ID: {}", tasks.size(), projectId);
            return tasks;

        } catch (Exception e) {
            log.error("An error occurred while retrieving tasks for project ID {}: {}", projectId, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve tasks for project ID: " + projectId, e);
        }
    }

    @Override
    public void removeTask(Long taskId, Long projectId) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "delete from Task t where t.id = :taskId and t.project.id = :projectId";
            int rowAffected = session.createQuery(hql)
                    .setParameter("taskId", taskId)
                    .setParameter("projectId", projectId)
                    .executeUpdate();
            transaction.commit();

            if (rowAffected == 0) {
                log.warn("Task with ID {} not found in project ID {}", taskId, projectId);
            } else {
                log.info("Task with ID {} removed successfully from project ID {}", taskId, projectId);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("An error occurred while removing task with ID {} from project ID {}: {}", taskId, projectId, e.getMessage(), e);
            throw new RuntimeException("Failed to remove task", e);
        }
    }

    @Override
    public void removeAllTask(Long projectId) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "delete from Task t where t.project.id = :projectId";
            int result = session.createQuery(hql)
                    .setParameter("projectId", projectId)
                    .executeUpdate();
            transaction.commit();

            if (result == 0) {
                log.warn("No task found with the project ID {}", projectId);
            } else {
                log.info("{} projects deleted for project ID: {}", result, projectId);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error occurred while deleting all tasks for project ID {}: {}", projectId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete tasks for project ID: " + projectId, e);
        }
    }

    @Override
    public void updateTask(Task task) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(task);
            transaction.commit();
            log.info("Task updated successfully");
            log.info("Task with ID {} updated successfully", task.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("An error occurred while updating task with ID {}: {}", task.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update task", e);
        }
    }
}
