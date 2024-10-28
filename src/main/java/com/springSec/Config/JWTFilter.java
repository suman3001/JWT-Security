package com.springSec.Config;

import com.springSec.Entity.AppUser;
import com.springSec.Repository.AppUserRepository;
import com.springSec.Service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private AppUserRepository appUserRepository;
    private JWTService jwtService;

    public JWTFilter(AppUserRepository appUserRepository, JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token!=null && token.startsWith("Bearer ")){
            String tokenValue = token.substring(8, token.length() - 1);

            String username = jwtService.getUsername(tokenValue);
            Optional<AppUser> opUsername = appUserRepository.findByUsername(username);
            if (opUsername.isPresent()){
                AppUser appUser = opUsername.get();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(appUser, null, Collections.singleton(new SimpleGrantedAuthority(appUser.getRole())));

                authenticationToken.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
