package com.eardh.gateway.server.plugin.pre.ratelimiter.extension;

import com.eardh.gateway.server.storage.ratelimiter.ApiRateLimiter;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.List;

import static com.eardh.gateway.common.model.RedisKey.REDIS_RATE_LIMITER_PREFIX;

public class RedisRateLimiter {

    private static final String LUA_SCRIPT = """
            local key = KEYS[1]
            local permits_key = KEYS[2]
            local now = tonumber(ARGV[1])
            local rate = tonumber(ARGV[2])
            local capacity = tonumber(ARGV[3])
            local requested_permits = tonumber(ARGV[4])
            local stored_permits = tonumber(redis.call('get', permits_key) or "0")
            local expected_permits = math.min(capacity, stored_permits + (now - redis.call('get', key) or 0) * rate)
            local allowed = expected_permits - requested_permits
            if allowed < 0 then
              return 0
            else
              redis.call('set', key, now)
              redis.call('set', permits_key, allowed)
              return 1
            end
                        """;

    private final RedisTemplate<String, String> redisTemplate;

    private final String key;

    private final RateLimiter rateLimiter;

    private final int capacity;

    private RedisScript<Long> rateLimitScript;

    public RedisRateLimiter(RedisTemplate<String, String> redisTemplate, ApiRateLimiter apiRateLimiter) {
        this.redisTemplate = redisTemplate;
        this.capacity = apiRateLimiter.getBucketCapacity();
        this.key = REDIS_RATE_LIMITER_PREFIX + apiRateLimiter.getApiId();
        this.rateLimiter = RateLimiter.create(apiRateLimiter.getLimiterRule());
        rateLimitScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
        fillBucket();
    }

    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    public boolean tryAcquire(int permits) {
        long now = System.currentTimeMillis();
        List<String> keys = Arrays.asList(key, key + ":permits");

        Double rate = rateLimiter.getRate();
        Object[] args = new Object[]{now, rate, capacity, permits};

        Long result = redisTemplate.execute(rateLimitScript, keys, args);

        return result != null && result == 1;
    }

    public void reset() {
        redisTemplate.delete(key);
        redisTemplate.delete(key + ":permits");
    }

    public void fillBucket() {
        long now = System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, String.valueOf(now));
        redisTemplate.opsForValue().set(key + ":permits", String.valueOf(capacity));
    }
}