package com.medelin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.refresh-key}")
    private String REFRESH_SECRET;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(UserDetails userDetails)
    {
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails)
    {
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token,  UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey()
    {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // ----------------------------
// RESET PASSWORD TOKEN LOGIC
// ----------------------------

    public String generateResetPasswordToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "RESET_PASSWORD");

        // 10 minutes expiration for reset tokens
        long resetExpiration = 1000 * 60 * 10;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + resetExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmailFromResetToken(String token)
    {
        Claims claims = extractAllClaims(token);

        Object type = claims.get("type");
        if (type == null || !type.equals("RESET_PASSWORD")) {
            throw new RuntimeException("Invalid reset token");
        }

        return claims.getSubject();
    }

    public boolean isResetToken(String token)
    {
        Claims claims = extractAllClaims(token);
        return "RESET_PASSWORD".equals(claims.get("type"));
    }

    public String generateRefreshToken(String email)
    {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET)
                .compact();
    }

    public String extractEmailFromRefreshToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(REFRESH_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isRefreshTokenValid(String token)
    {
        try {
            Jwts.parser().setSigningKey(REFRESH_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
