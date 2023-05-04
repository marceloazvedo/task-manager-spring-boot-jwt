package br.com.marcelo.azevedo.controller.exchange;

public record CreateUserRequest(
        String username,
        String password
) {

}