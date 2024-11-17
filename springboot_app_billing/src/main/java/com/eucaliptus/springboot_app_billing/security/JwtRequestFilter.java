package com.eucaliptus.springboot_app_billing.security;

import com.eucaliptus.springboot_app_products.utlities.ServicesUri;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tokenHeader = request.getHeader("Authorization");
            if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);
                //if (isValidToken(token)) {
                    String username = jwtTokenUtil.extractUsername(token);
                    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority(jwtTokenUtil.extractAllClaims(token).get("role", String.class))
                    );
                    UserDetails userDetails = new User(username, username, authorities);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                //}
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        HttpEntity<String> entity = new HttpEntity<>(token, getHeader(token).getHeaders());
        ResponseEntity<Boolean> response = restTemplate.exchange(
                ServicesUri.AUTH_SERVICE + "/auth/validate",
                HttpMethod.POST,
                entity,
                Boolean.class
        );
        return response.getBody();
    }

    private HttpEntity<String> getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}

