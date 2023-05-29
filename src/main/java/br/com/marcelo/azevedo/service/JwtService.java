package br.com.marcelo.azevedo.service;

import br.com.marcelo.azevedo.controller.exchange.GenerateJwtRequest;
import br.com.marcelo.azevedo.controller.exchange.JwtGeneratedResponse;
import br.com.marcelo.azevedo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public static final String JWT_SECRET_VALUE = "THIS_IS_MY_SECRET";

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public JwtService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public JwtGeneratedResponse generateAccessToken(final GenerateJwtRequest generateJwtRequest) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        generateJwtRequest.username(),
                        generateJwtRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var user = userRepository.findByUsername(generateJwtRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        Claims claims = Jwts.claims().setSubject(user.getId());
        claims.put("username", user.getUsername());
        claims.put("password", user.getPassword());
        final var jwt = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_VALUE)
                .compact();

        return new JwtGeneratedResponse(user.getId(), user.getUsername(), jwt);
    }

}
