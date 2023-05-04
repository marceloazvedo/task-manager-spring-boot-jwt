package br.com.marcelo.azevedo.filter;

import br.com.marcelo.azevedo.domain.ApplicationUserContext;
import br.com.marcelo.azevedo.exception.InvalidAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

import static br.com.marcelo.azevedo.service.JwtService.JWT_SECRET_VALUE;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            validateAuthorizationBearer(request);
            final var accessToken = getAccessToken(request);
            validateAccessToken(accessToken);
            setAuthenticationContext(accessToken, request);
        } catch (InvalidAuthenticationException exception) {
            exception.printStackTrace();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void validateAuthorizationBearer(final HttpServletRequest request) {
        final var header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            throw new InvalidAuthenticationException();
        }
    }

    private String getAccessToken(final HttpServletRequest request) {
        final var header = request.getHeader("Authorization");
        if (!header.contains(" ")) throw new InvalidAuthenticationException();
        return header.split(" ")[1].trim();
    }

    private void validateAccessToken(final String accessToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_VALUE).parseClaimsJws(accessToken);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InvalidAuthenticationException();
        }
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        final var userDetails = getUserDetails(token);
        final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String accessToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_VALUE)
                .parseClaimsJws(accessToken)
                .getBody();
        final var userId = claims.getSubject();
        final var username = (String) claims.get("username");
        return new ApplicationUserContext(userId, username, null, new ArrayList<>());
    }

}
