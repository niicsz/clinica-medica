package com.example.clinica_medica.security;

import java.time.Instant;

public record JwtToken(String value, Instant expiresAt) {}
