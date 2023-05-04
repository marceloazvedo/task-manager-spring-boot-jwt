package br.com.marcelo.azevedo.controller;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.facade.TaskFacade;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskFacade taskFacade;

    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(
                taskFacade.createTask(taskRequest),
                HttpStatus.CREATED
        );
    }

    @RequestMapping("/{date}")
    public ResponseEntity<ListTaskThatFinishInResponse> listThatFinishIn(
            @PathParam("date") final String date
    ) {
        return ResponseEntity.ok(taskFacade.listThatFinishIn(date));
    }

}
