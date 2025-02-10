package com.kunmi.taskManager.repository.userRepo;


import com.google.gson.Gson;
import com.kunmi.taskManager.models.User;
import com.kunmi.taskManager.utils.hibernate.HibernateUtil;
import com.kunmi.taskManager.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.SetParams;

import java.util.List;

@Slf4j
public class UserRepositoryImpl implements  UserRepository{

    public void saveUser(User user) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            //saveUserToRedis(user);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Error occurred while saving user: {}", e.getMessage());
            throw e;
        }
    }

    private void saveUserToRedis(User user) {
        try(Jedis jedis = RedisUtil.getJedis()) {
            String userJson = new Gson().toJson(user);

            SetParams setParams = new SetParams();
            jedis.set(user.getEmail(), userJson, setParams.ex(60));

            log.info("User saved to Redis: {}", user.getEmail());
        } catch (JedisException e) {
            log.error("Error saving user to Redis - {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public User getUserByEmail(String email) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    @Override
    public void updateUser(User user) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();
            log.info("User updated successfully.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error occurred while updating user - {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteUser(String email) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "delete from User u where u.email = :email";
            int result = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .executeUpdate();

            transaction.commit();

            if (result == 0) {
                log.warn("No user found with the email {}", email);
            } else {
                log.info("User with email: {} is deleted successfully", email);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting user with email {}", email);
            throw e;
        }

    }

    @Override
    public List<User> getAllUsers() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public boolean userExists(String email) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(u) from User u where u.email = :email";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

}
