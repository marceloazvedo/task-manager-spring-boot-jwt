package br.com.marcelo.azevedo.controller.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskRequest(
    String name,
    String description,
    @JsonProperty("is_to_finish_at")
    String isToFinishAt
) {
}
