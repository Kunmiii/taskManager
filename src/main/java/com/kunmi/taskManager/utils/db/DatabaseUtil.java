package com.kunmi.taskManager.utils.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/TaskManager");
            config.setUsername("postgres");
            config.setPassword("Kunmi568.3");
            config.setDriverClassName("org.postgresql.Driver");

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);
            config.setValidationTimeout(5000);

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Database connection pool initialized failed: " + e.getMessage());
        }
    }

    public DatabaseUtil() throws SQLException {
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void closePool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
