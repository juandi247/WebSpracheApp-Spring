package com.sprache.juandiegodeutsch.jwt;

import com.sprache.juandiegodeutsch.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;



    // Filters incoming requests and validates JWT token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = getTokenFromRequest(request);
        final String username;



        if (token ==null){
            filterChain.doFilter(request,response);
            return;
        }

        username= jwtService.getUsernameFromToken(token);
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token,userDetails))
            {
                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails
                                .getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }

    // Prevents filter from applying on certain routes (the ones from AuthController)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Excluding public routes
        return path.startsWith("/auth/");
    }



    // Prevents filter from applying on certain routes (e.g., public login routes)
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}


