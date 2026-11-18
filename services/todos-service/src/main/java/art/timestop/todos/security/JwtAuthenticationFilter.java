package art.timestop.todos.security;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value ("${spring.jwt.secret}")
      private String secretKey;

      @Override
      protected void doFilterInternal(
              HttpServletRequest request,
              HttpServletResponse response,
              FilterChain filterChain)
              throws ServletException, IOException {

          String authHeader = request.getHeader("Authorization");

          if (authHeader == null || !authHeader.startsWith("Bearer ")) {
              filterChain.doFilter(request, response);
              return;
          }

          try {
              String token = authHeader.substring(7);

              byte[] keyBytes = Decoders.BASE64.decode(secretKey);
              SecretKey key =   Keys.hmacShaKeyFor(keyBytes);

              Claims claims = Jwts.parser()
                      .verifyWith(key)
                      .build()
                      .parseSignedClaims(token)
                      .getPayload();

              String userId = claims.get("userId").toString();

              UsernamePasswordAuthenticationToken authentication =
                      new UsernamePasswordAuthenticationToken(
                              userId,
                              null,
                              List.of()
                      );

              SecurityContextHolder.getContext()
                      .setAuthentication(authentication);

          } catch (Exception ex) {
              SecurityContextHolder.clearContext();
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("application/json");
              response.getWriter().write(
                      "{\"error\":\"Invalid or expired JWT\"}"
              );
              return;
          }

          filterChain.doFilter(request, response);
      }
}
