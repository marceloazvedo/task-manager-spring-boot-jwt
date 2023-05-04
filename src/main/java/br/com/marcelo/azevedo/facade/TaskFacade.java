package br.com.marcelo.azevedo.facade;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskFacade {

    @Autowired
    private TaskService taskService;

    public TaskResponse createTask(TaskRequest taskRequest) {
        return taskService.create(taskRequest);
    }

    public ListTaskThatFinishInResponse listThatFinishIn(String date) {
        return taskService.listThatFinishIn(date);
    }

}
