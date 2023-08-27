package dev.gabriel.bugtracker.Configuration;

import dev.gabriel.bugtracker.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //get the header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        //if the header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //pass to next filter
            filterChain.doFilter(request, response);
            return;
        }

        //grab jwt token from header
        jwtToken = authHeader.substring(7);
        //grab user email from the jwt token
        userEmail = jwtService.extractEmail(jwtToken);

        //if the user email is not null and there is no existing authentication in the context holder
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //load details from the user
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            //if JWT token is valid create username password auth token and set it in the context holder
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newJwtToken = jwtService.generateToken(userDetails);
        response.setHeader("Authorization", "Bearer " + newJwtToken);
        //pass to next filter
        filterChain.doFilter(request, response);
    }
}
