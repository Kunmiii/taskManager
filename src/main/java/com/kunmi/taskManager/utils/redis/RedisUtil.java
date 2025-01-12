package com.kunmi.taskManager.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static final JedisPool jedisPool;

    static {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();

            poolConfig.setMaxTotal(128);
            poolConfig.setMaxIdle(64);
            poolConfig.setMinIdle(16);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);

            jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Redis connection pool initialization failed: " + e.getMessage());
        }
    }
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null && !jedisPool.isClosed()) {
            jedis.close();
        }
    }

}
