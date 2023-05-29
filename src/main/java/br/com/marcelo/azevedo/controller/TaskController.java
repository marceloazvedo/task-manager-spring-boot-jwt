package br.com.marcelo.azevedo.controller;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.facade.TaskFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskFacade taskFacade;

    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(
                taskFacade.createTask(taskRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{dateToTasksThatFinishesIn}")
    public ResponseEntity<ListTaskThatFinishInResponse> listThatFinishIn(
            @PathVariable("dateToTasksThatFinishesIn") final String dateToTasksThatFinishesIn
    ) {
        return ResponseEntity.ok(taskFacade.listThatFinishIn(dateToTasksThatFinishesIn));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Object> markTaskAsFinished(
            @PathVariable("taskId") final String taskId
    ) {
        taskFacade.markAsFinished(taskId);
        return ResponseEntity.noContent().build();
    }

}
