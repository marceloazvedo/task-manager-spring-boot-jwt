package br.com.marcelo.azevedo.controller.exchange;

import java.util.List;

public record ListTaskThatFinishInResponse(
        String dateToFinish,
        List<TaskResponse> tasks
) {
}
