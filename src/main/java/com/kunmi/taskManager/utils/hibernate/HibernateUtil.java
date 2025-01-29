package com.kunmi.taskManager.utils.hibernate;

import lombok.Getter;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            log.info("SessionFactory created successfully.");
        } catch (HibernateException e) {
            log.error("SessionFactory creation failed.{}", e.getMessage());
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: "+ e.getMessage());
        }
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            getSessionFactory().close();
            log.info("SessionFactory shut down successfully.");
        }

    }
}