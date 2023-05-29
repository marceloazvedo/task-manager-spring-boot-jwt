package br.com.marcelo.azevedo.facade;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.service.TaskService;
import br.com.marcelo.azevedo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class TaskFacade {

    private final TaskService taskService;
    private final UserService userService;

    public TaskFacade(
            TaskService taskService,
            UserService userService
    ) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public TaskResponse createTask(TaskRequest taskRequest) {
        final var userRequesting = userService.getUserRequesting();
        final var newTaskCreated = taskService.create(taskRequest, userRequesting);
        return taskService.mapper(newTaskCreated);
    }

    public ListTaskThatFinishInResponse listThatFinishIn(String dateToFindTasksThatFinishesIn) {
        final var userRequesting = userService.getUserRequesting();
        final var allTasksOfUser = taskService.findAllByUserIdOwner(userRequesting.getId());
        final var allTasksOfUserThatFinshInDate = taskService.listThatFinishIn(allTasksOfUser, dateToFindTasksThatFinishesIn);
        return taskService.mapper(dateToFindTasksThatFinishesIn, allTasksOfUserThatFinshInDate);
    }

    public void markAsFinished(String taskId) {
        final var userRequesting = userService.getUserRequesting();
        final var taskFinded = taskService.findByIdAndUserOwner(taskId, userRequesting.getId());
        taskService.markAsFinished(taskFinded);
    }



}
