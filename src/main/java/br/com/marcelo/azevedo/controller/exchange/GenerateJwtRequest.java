package br.com.marcelo.azevedo.controller.exchange;

public record GenerateJwtRequest(
        String username,
        String password
) {
}
