package com.fatou.apiqr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper mapper;

    public TokenFilter(JwtService jwtService, ObjectMapper mapper) {
        this.jwtService = jwtService;
        this.mapper = mapper;

}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


    try {
        String accessToken = jwtService.resolveToken(request);
        if (accessToken == null ) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("token : "+accessToken);
        Claims claims = jwtService.resolveClaims(request);

        if(claims != null & jwtService.validateClaims(claims)){
            String email = claims.getSubject();
            List<String> roles = jwtService.getRoles(claims);
            System.out.println("username : "+email);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    }catch (Exception e){
        HashMap<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Authentication Error");
        errorDetails.put("details",e.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        logger.error("AUTH ERROR: ", e);

        mapper.writeValue(response.getWriter(), errorDetails);

    }
        filterChain.doFilter(request, response);
}
}

