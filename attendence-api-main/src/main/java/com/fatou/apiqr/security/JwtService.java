package com.fatou.apiqr.security;/*
 * @author
 * created on 04/10/2023
 * @project IntelliJ IDEA
 */

import com.fatou.apiqr.models.UserModel;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtService {
    public static long EXPIRATION_TIME = 60 * 60 * 1000;
    //public static long EXPIRATION_TIME = -1;
    public static String TOKEN_PREFIX = "Bearer ";
    public static String HEADER_STRING = "Authorization";
    public static String SECRET = "0MLjwttestDevTest";

    private final UserDetailsService userDetailsService;
    private JwtParser jwtParser;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtParser = Jwts.parser().setSigningKey(SECRET);
    }

    public String createToken(UserModel user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", Collections.singletonList(user.getRole()));
        claims.put("Nom",user.getNom());
        claims.put("Prenom",user.getPrenom());
        claims.put("Email",user.getEmail());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(EXPIRATION_TIME));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();


    }
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    public List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

}


