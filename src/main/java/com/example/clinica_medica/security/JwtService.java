package com.example.clinica_medica.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final Key signingKey;
  private final Duration expiration;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.expiration}") String expiration) {
    this.signingKey = createSigningKey(secret);
    this.expiration = Duration.parse(expiration);
  }

  public JwtToken generateToken(UserDetails userDetails) {
    Instant now = Instant.now();
    Instant expiresAt = now.plus(expiration);

    String token =
        Jwts.builder()
            .setClaims(
                Map.of(
                    "roles",
                    userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .toList()))
            .setSubject(userDetails.getUsername())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiresAt))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();

    return new JwtToken(token, expiresAt);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    return claimsResolver.apply(claims);
  }

  private Key getSigningKey() {
    return signingKey;
  }

  private Key createSigningKey(String secret) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
      return Keys.hmacShaKeyFor(hashed);
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 algorithm not available", ex);
    }
  }
}
