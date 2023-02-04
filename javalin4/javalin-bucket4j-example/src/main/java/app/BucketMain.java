package app;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.remote.RemoteBucketState;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;

import java.time.Duration;
import java.util.function.Supplier;


public class BucketMain {

    public static final int SERVER_PORT = 7070;

    // Specifies how long bucket can be held in the cache after all consumed tokens have been refilled.
    // Greater value helps to avoid unnecessary reinitialization of buckets
    public static final Duration KEEP_AFTER_REFILL_DURATION = Duration.ofMinutes(1);

    // the limit for cache size
    public static final int MAX_CACHED_BUCKETS = 100_000;

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(SERVER_PORT);
        Caffeine<String, RemoteBucketState> cacheBuilder = Caffeine.newBuilder()
            .maximumSize(MAX_CACHED_BUCKETS)
            .evictionListener((String k, RemoteBucketState s, RemovalCause cause) -> System.out.println("Key " + k + " got removed because of " + cause));

        ProxyManager<String> buckets = new CaffeineProxyManager<>(cacheBuilder, KEEP_AFTER_REFILL_DURATION);

        app.get("/", ctx -> {
            String limitingKey = getRateLimitingKey(ctx);
            Supplier<BucketConfiguration> limitingConfigSupplier = getRateLimitingConfig(ctx);
            int requestWeight = getRequestWeight(ctx);
            Bucket limiter = buckets.builder().build(limitingKey, limitingConfigSupplier);
            if (!limiter.tryConsume(requestWeight)) {
                throw new HttpResponseException(429, "Too many requests");
            }
            ctx.result("Hello World");
        });
    }

    private static int getRequestWeight(Context ctx) {
        // request weight can depend from concrete API
        return 1;
    }

    private static Supplier<BucketConfiguration> getRateLimitingConfig(Context ctx) {
        // rate limits can depend from concrete API
        return () -> BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(10, Refill.greedy(5, Duration.ofMinutes(1))))
            .build();
    }

    private static String getRateLimitingKey(Context ctx) {
        // Way to choose rate limiting key can depend on from concrete API
        return ctx.ip();
    }
}
