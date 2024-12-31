package com.kunmi.taskManager.utils.redis;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    public static Jedis getJedis() {
        return new Jedis(REDIS_HOST, REDIS_PORT);
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
