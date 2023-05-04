package br.com.marcelo.azevedo.controller.exchange;

public record JwtGeneratedResponse(
        String userId,
        String username,
        String accessToken
) {
}
