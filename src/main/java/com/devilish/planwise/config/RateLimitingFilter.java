package com.devilish.planwise.config;

// import com.github.bucket4j.Bucket;
// Imports comentados pois a classe está desabilitada
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpStatus;
// import org.springframework.lang.NonNull;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import java.io.IOException;
// import java.util.Map;

// @Component
// @RequiredArgsConstructor
// public class RateLimitingFilter extends OncePerRequestFilter {

//     private final Map<String, Bucket> rateLimitBuckets;

//     @Override
//     protected void doFilterInternal(
//             @NonNull HttpServletRequest request,
//             @NonNull HttpServletResponse response,
//             @NonNull FilterChain filterChain
//     ) throws ServletException, IOException {

//         String clientIp = getClientIpAddress(request);
//         String requestPath = request.getRequestURI();
//         
//         // Determinar limite baseado no endpoint
//         Bucket bucket = getBucketForRequest(clientIp, requestPath);
//         
//         if (bucket.tryConsume(1)) {
//             filterChain.doFilter(request, response);
//         } else {
//             response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//             response.setContentType("application/json");
//             response.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\"}");
//         }
//     }

//     private Bucket getBucketForRequest(String clientIp, String requestPath) {
//         String key = clientIp + ":" + requestPath;
//         
//         return rateLimitBuckets.computeIfAbsent(key, k -> {
//             if (requestPath.contains("/api/auth/login")) {
//                 return RateLimitingConfig.createBucket(
//                     RateLimitingConfig.LOGIN_RATE_LIMIT, 
//                     RateLimitingConfig.LOGIN_RATE_LIMIT, 
//                     RateLimitingConfig.RATE_LIMIT_WINDOW
//                 );
//             } else if (requestPath.contains("/api/auth/register")) {
//                 return RateLimitingConfig.createBucket(
//                     RateLimitingConfig.REGISTER_RATE_LIMIT, 
//                     RateLimitingConfig.REGISTER_RATE_LIMIT, 
//                     RateLimitingConfig.RATE_LIMIT_WINDOW
//                 );
//             } else {
//                 return RateLimitingConfig.createBucket(
//                     RateLimitingConfig.GENERAL_RATE_LIMIT, 
//                     RateLimitingConfig.GENERAL_RATE_LIMIT, 
//                     RateLimitingConfig.RATE_LIMIT_WINDOW
//                 );
//             }
//         });
//     }

//     private String getClientIpAddress(HttpServletRequest request) {
//         String xForwardedFor = request.getHeader("X-Forwarded-For");
//         if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
//             return xForwardedFor.split(",")[0].trim();
//         }
//         
//         String xRealIp = request.getHeader("X-Real-IP");
//         if (xRealIp != null && !xRealIp.isEmpty()) {
//             return xRealIp;
//         }
//         
//         return request.getRemoteAddr();
//     }
// }
