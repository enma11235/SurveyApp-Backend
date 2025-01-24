package com.github.enma11235.generic.poll.system.security;

import com.github.enma11235.generic.poll.system.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    //cada una de las operaciones de esta clase deberia lanzar una excepcion si el token no es valido

    public final SecretKey secretKey;
    private final long tokenExpirationTime;

    @Autowired
    public JwtTokenProvider(SecretKey secretKey, long tokenExpirationTime) {
        this.secretKey = secretKey;
        this.tokenExpirationTime = tokenExpirationTime;
    }

    // GENERATE TOKEN
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime)) //REVISAR ESTO
                .claim("id", user.getId())
                .claim("role", "user")
                .signWith(SignatureAlgorithm.HS256, secretKey) //REVISAR ESTO
                .compact();
    }

    // VALIDATE TOKEN
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); //REVISAR ESTO
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // GET USERNAME FROM TOKEN
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}