package com.example.clinica_medica.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private static final String BEARER_PREFIX = "Bearer ";
  private static final String TOKEN_COOKIE_NAME = "jwt-token";

  @Autowired private JwtService jwtService;

  @Autowired private CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();
    logger.debug("Processando requisição: {} {}", request.getMethod(), requestURI);

    String token = extractToken(request);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        String username = jwtService.extractUsername(token);
        if (username != null) {
          logger.debug("Token JWT encontrado para usuário: {}", username);
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          if (jwtService.isTokenValid(token, userDetails)) {
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Autenticação JWT bem-sucedida para usuário: {}", username);
          } else {
            logger.warn("Token JWT inválido para usuário: {}", username);
          }
        }
      } catch (Exception e) {
        logger.error("Erro ao processar token JWT: {}", e.getMessage());
        SecurityContextHolder.clearContext();
      }
    } else if (token == null) {
      logger.trace("Nenhum token JWT encontrado para: {}", requestURI);
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
      return authHeader.substring(BEARER_PREFIX.length());
    }

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
