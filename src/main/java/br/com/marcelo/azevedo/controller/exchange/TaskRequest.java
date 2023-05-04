package br.com.marcelo.azevedo.controller.exchange;

public record TaskRequest(
    String name,
    String description,
    String isToFinishAt
) {
}
