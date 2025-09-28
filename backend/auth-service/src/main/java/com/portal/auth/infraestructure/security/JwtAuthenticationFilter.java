package com.portal.auth.infraestructure.security;

import com.portal.auth.application.UserService;
import com.portal.auth.domain.model.User;
import com.portal.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                try {
                    String subject = jwtUtil.getSubject(token);
                    // subject -> esperamos userId como string
                    Long userId = Long.valueOf(subject);
                    Optional<User> maybeUser = userService.getUser(userId);
                    if (maybeUser.isPresent()) {
                        User user = maybeUser.get();
                        // Se puede construir un Authentication con roles si quieres
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, java.util.Collections.emptyList());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (Exception e) {
                    // token inválido -> continúa sin auth
                    }
            }
        }
        filterChain.doFilter(request, response);
    }
}
