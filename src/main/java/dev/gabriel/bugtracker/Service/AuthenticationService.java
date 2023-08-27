package dev.gabriel.bugtracker.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabriel.bugtracker.Model.User;
import dev.gabriel.bugtracker.Repository.UserRepository;
import dev.gabriel.bugtracker.Authentication.AuthenticationRequest;
import dev.gabriel.bugtracker.Authentication.AuthenticationResponse;
import dev.gabriel.bugtracker.Authentication.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;

    //user registration
    public AuthenticationResponse register(RegisterRequest request) {
        //create a user object with provided details
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        //save to the database
        repository.save(user);

        //generate a JWT token for the user
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    //user authentication
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //authenticate the user with the provided email and password
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        //grab user from the db with provided email
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        //generate new JWT token for the user
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        //if the header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //pass to next filter
            return;
        }

        //grab jwt token from header
        refreshToken = authHeader.substring(7);
        //grab user email from the jwt token
        userEmail = jwtService.extractEmail(refreshToken);

        //if the user email is not null and there is no existing authentication in the context holder
        if (userEmail != null) {
            //load details from the user
            var userDetails = this.repository.findByEmail(userEmail).orElseThrow();
            //if JWT token is valid create username password auth token and set it in the context holder
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}