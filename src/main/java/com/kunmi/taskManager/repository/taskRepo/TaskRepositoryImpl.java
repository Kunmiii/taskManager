package com.kunmi.taskManager.repository.taskRepo;

import com.kunmi.taskManager.models.Task;
import com.kunmi.taskManager.utils.hibernate.HibernateUtil;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskRepositoryImpl implements TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    @Override
    public void addTask(Task task) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(task);

            transaction.commit();
            log.info("Task created successfully");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error occurred while adding task to database {}", e.getMessage());
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public Task getTask(Long taskId, Long projectId) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "from Task t where t.id = :taskId and t.project.id = :projectId";

            return session.createQuery(hql, Task.class)
                    .setParameter("taskId", taskId)
                    .setParameter("projectId", projectId)
                    .uniqueResult();

        } catch (Exception e) {
            log.error("Error occurred while retrieving task with ID {} for project ID {}: {}",
                    taskId, projectId, e.getMessage());
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public List<Task> getProjectTasks(Long projectId) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Task t where t.project.id = :projectId", Task.class)
                    .setParameter("projectId", projectId)
                    .list();

        } catch (Exception e) {
            log.error("An error occurred while retrieving task from the database");
            throw e;
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
                log.warn("Task not found!");
            } else {
                log.info("Task with ID {} for user {} removed successfully", projectId, taskId);
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
            log.error("Error occurred while deleting all project with ID: {}", projectId);
            throw e;
        }
    }

    @Override
    public void updateTask(Task task) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(task);
            log.info("Task updated successfully");

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("An error occurred while updating task: {}", e.getMessage());
            throw e;
        }
    }
}
