package br.com.marcelo.azevedo.service;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.entity.TaskEntity;
import br.com.marcelo.azevedo.entity.UserEntity;
import br.com.marcelo.azevedo.exception.TaskNotFoundException;
import br.com.marcelo.azevedo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.marcelo.azevedo.util.Constants.FORMATTER_YYYY_MM_DD;
import static br.com.marcelo.azevedo.util.LocalDateTimeConverter.getByShortStringFormat;
import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateTaskId;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public TaskEntity create(TaskRequest taskRequest, UserEntity userRequesting) {
        final var taskEntity = new TaskEntity(
                generateTaskId(),
                taskRequest.name(),
                taskRequest.description(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                getByShortStringFormat(taskRequest.isToFinishAt()),
                userRequesting.getId(),
                Boolean.FALSE,
                null
        );
        return taskRepository.save(taskEntity);
    }

    public List<TaskEntity> findAllByUserIdOwner(String userOwnerId) {
        return taskRepository.findAllByBelongsToUserId(userOwnerId);
    }

    public List<TaskEntity> listThatFinishIn(List<TaskEntity> allTasksOfUser, String date) {
        final var allTasksOfUserThatFinishIn = allTasksOfUser.parallelStream()
                .filter(task -> task.getIsToFinishAt().format(FORMATTER_YYYY_MM_DD).equals(date))
                .toList();
        if(allTasksOfUserThatFinishIn.isEmpty()) throw new TaskNotFoundException();
        return allTasksOfUserThatFinishIn;
    }

    public TaskResponse mapper(TaskEntity taskEntity) {
        return new TaskResponse(
                taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getDescription(),
                FORMATTER_YYYY_MM_DD.format(taskEntity.getIsToFinishAt()),
                taskEntity.getFinished()
        );
    }

    public ListTaskThatFinishInResponse mapper(String finishIn, List<TaskEntity> tasksThatFinishIn) {
        return new ListTaskThatFinishInResponse(
                finishIn,
                tasksThatFinishIn.stream().map(this::mapper).toList()
        );
    }

    public TaskEntity findByIdAndUserOwner(String taskId, String userOwnerId) {
        return taskRepository.findByIdAndBelongsToUserId(taskId, userOwnerId)
                .orElseThrow(TaskNotFoundException::new);
    }

    public void markAsFinished(TaskEntity taskToMarkAsFinished) {
        taskToMarkAsFinished.setFinished(true);
        taskToMarkAsFinished.setFinishedAt(LocalDateTime.now());
        taskRepository.save(taskToMarkAsFinished);
    }
}
