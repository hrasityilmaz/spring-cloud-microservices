
package art.timestop.apigateway.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter
        extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || authHeader.isBlank()) {
                return onError(exchange, "Missing Authorization Header");
            }

            if (!authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization Header");
            }

            String jwt = authHeader.substring(7).trim();
            if (jwt.isEmpty()) {
                return onError(exchange, "JWT token is empty");
            }
            if (!isJwtValid(jwt)) {
                return onError(exchange, "Invalid or expired JWT");
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(
            ServerWebExchange exchange,
            String message) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(
                org.springframework.http.MediaType.APPLICATION_JSON);
        String body = "{\"error\":\"" + message + "\"}";
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(
                        body.getBytes(java.nio.charset.StandardCharsets.UTF_8)
                ))
        );
    }

    private boolean isJwtValid(String jwt) {

        try {
            // Users service ile aynı Base64 secret kullanılır
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);

            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            // İmza ve expiration kontrolü
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            return claims.getSubject() != null
                    && !claims.getSubject().isBlank();

        } catch (Exception ex) {
            return false;
        }
    }
}
