package com.askver.sky.userdetail;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
@Component
public class JwtUtil {

    private final String SECRET =
            "my-super-secret-key-my-super-secret-key-123456";

    private final long EXPIRATION =
            1000 * 60 * 60 * 10;

    public String generateToken(
            CustomUserDetails user
    ) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId",
                        user.getUserId())
                .claim("companyId",
                        user.getCompanyId())
                .claim("role",
                        user.getAuthorities()
                                .iterator()
                                .next()
                                .getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION
                        )
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        ),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public Claims extractClaims(
            String token
    ) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        SECRET.getBytes()
                )
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}