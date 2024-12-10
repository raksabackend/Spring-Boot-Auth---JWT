package com.example.demo.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime; // in milliseconds

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the username for which the token is generated
     * @return a signed JWT token
     */
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return verifyToken(token).getSubject();
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        Date expirationDate = verifyToken(token).getExpiresAt();
        return expirationDate.before(new Date());


    }

    /**
     * Verifies the given JWT token and returns its decoded form.
     *
     * @param token the JWT token to verify
     * @return DecodedJWT object containing claims from the verified token
     * @throws JWTVerificationException if verification fails
     */
    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid or expired JWT token", e);
        }
    }

    /**
     * Extracts custom claims from the given JWT token.
     *
     * @param token the JWT token
     * @return a map of claims
     */
    public Map<String, Object> extractAllClaims(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        Map<String, Object> claims = new HashMap<>();

        // Add any custom claims you want to extract here
        claims.put("username", decodedJWT.getSubject());
        claims.put("issuedAt", decodedJWT.getIssuedAt());
        claims.put("expiration", decodedJWT.getExpiresAt());

        // Add more claims as needed
        return claims;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
