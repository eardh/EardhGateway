import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisScript<Long> tryAcquireScript;

    private final String key;

    private final int permitsPerSecond;

    public RedisRateLimiter(String key, int permitsPerSecond, RedisTemplate<String, String> redisTemplate) {
        this.key = key;
        this.permitsPerSecond = permitsPerSecond;
        this.redisTemplate = redisTemplate;

        String script = "local rate = tonumber(ARGV[1]) " +
                "local capacity = rate " +
                "local now = tonumber(redis.call('time')[1]) * 1000 + tonumber(redis.call('time')[2]) / 1000 " +
                "local last = tonumber(redis.call('get', KEYS[1]) or '0|0') " +
                "local lastTokens = tonumber(string.sub(last, 1, string.find(last, '|') - 1)) " +
                "local lastMillis = tonumber(string.sub(last, string.find(last, '|') + 1)) " +
                "local elapsed = math.max(0, now - lastMillis) " +
                "local generated = math.floor(elapsed * rate / 1000) " +
                "local tokens = math.min(capacity, lastTokens + generated) " +
                "local delay = 0 " +
                "if tokens < 1 then " +
                "    delay = (1 - tokens / rate) * 1000 " +
                "end " +
                "redis.call('set', KEYS[1], tokens .. '|' .. now) " +
                "redis.call('pexpire', KEYS[1], math.ceil(1000 * capacity / rate)) " +
                "return delay ";

        tryAcquireScript = new DefaultRedisScript<>(script, Long.class);
    }

    public boolean tryAcquire() {
        Long delay = redisTemplate.execute(tryAcquireScript, List.of(key), String.valueOf(permitsPerSecond));
        if (delay > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }
}