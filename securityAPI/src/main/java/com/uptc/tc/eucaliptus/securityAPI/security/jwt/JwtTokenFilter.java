package com.uptc.tc.eucaliptus.securityAPI.security.jwt;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.TokenService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tokenHeader = request.getHeader("Authorization");
            if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);
                if (jwtProvider.isTokenValid(token) && tokenService.existsToken(token)) {
                    String username = jwtProvider.getUsernameFromToken(token);
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }


}
