package com.ecommerce.sb_ecomm.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtExpiration}")
    private int jwtExpirationMs;
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Token: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUserName(UserDetails userDetails) {
        String userName = userDetails.getUsername();
        return Jwts.builder().subject(userName).
                issuedAt(new Date()).
                expiration(new Date((new Date().getTime() + jwtExpirationMs))).
                signWith(key()).
                compact();
    }

    public String getUserNameFromJWTToken(String token) {
        return Jwts.parser().
                verifyWith((SecretKey) key()).
                build().parseSignedClaims(token).getPayload().getSubject();
    }

    private Key key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateJwtToken(String token) {
        try {
            System.out.println("Validate JWS Toklen");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }
}

