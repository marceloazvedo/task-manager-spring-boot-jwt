package br.com.marcelo.azevedo.controller.exchange;

public record TaskResponse(
        String id,
        String name,
        String note,
        String needFinishIn
) {
}
