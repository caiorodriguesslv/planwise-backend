package com.devilish.planwise.config;

// import com.github.bucket4j.Bucket;
// import com.github.bucket4j.Bucket4j;
// import com.github.bucket4j.Bandwidth;
// import com.github.bucket4j.Refill;

import org.springframework.context.annotation.Configuration;

// import java.time.Duration;



@Configuration
public class RateLimitingConfig {

    // @Bean
    // public Map<String, Bucket> rateLimitBuckets() {
    //     return new ConcurrentHashMap<>();
    // }

    // public static Bucket createBucket(int capacity, int refillTokens, Duration refillDuration) {
    //     return Bucket4j.builder()
    //             .addLimit(Bandwidth.classic(capacity, Refill.intervally(refillTokens, refillDuration)))
    //             .build();
    // }

    // Configurações específicas para diferentes endpoints
    public static final int LOGIN_RATE_LIMIT = 5; // 5 tentativas
    public static final int REGISTER_RATE_LIMIT = 3; // 3 tentativas
    public static final int GENERAL_RATE_LIMIT = 100; // 100 requisições
    // public static final Duration RATE_LIMIT_WINDOW = Duration.ofMinutes(1); // por minuto
}
