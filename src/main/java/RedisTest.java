import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            String key = "000";
            String passwordHash = "$2a$10$EIXgXXDQ26wFzvnzeqqeCOU3rRtQn/muDWU0XoxvABzkhrTLf1Z12";

            // Store the hash
            jedis.set(key, passwordHash);

            // Retrieve the hash
            String retrievedHash = jedis.get(key);
            System.out.println("Retrieved Hash: " + retrievedHash);

            // Verify the hash is unchanged
            if (passwordHash.equals(retrievedHash)) {
                System.out.println("Redis storage test successful.");
            } else {
                System.err.println("Mismatch in stored and retrieved hash.");
            }
        }
    }
}
