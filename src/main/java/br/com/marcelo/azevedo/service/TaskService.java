package br.com.marcelo.azevedo.service;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.entity.TaskEntity;
import br.com.marcelo.azevedo.entity.UserEntity;
import br.com.marcelo.azevedo.repository.TaskRepository;
import br.com.marcelo.azevedo.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateTaskId;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskEntity create(TaskRequest taskRequest, UserEntity userRequesting) {
        final var taskEntity = new TaskEntity(
                generateTaskId(),
                taskRequest.name(),
                taskRequest.description(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDate.parse(taskRequest.isToFinishAt(), formatter).atStartOfDay(),
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
                .filter(task -> task.getIsToFinishAt().format(formatter).equals(date))
                .toList();
        if(allTasksOfUserThatFinishIn.isEmpty()) throw new NotFoundException("Has not task to list!");
        return allTasksOfUserThatFinishIn;
    }

    public TaskResponse mapper(TaskEntity taskEntity) {
        return new TaskResponse(
                taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getDescription(),
                formatter.format(taskEntity.getIsToFinishAt()),
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
                .orElseThrow(() -> new NotFoundException("Task not found for this user."));
    }

    public void markAsFinished(TaskEntity taskToMarkAsFinished) {
        taskToMarkAsFinished.setFinished(true);
        taskToMarkAsFinished.setFinishedAt(LocalDateTime.now());
        taskRepository.save(taskToMarkAsFinished);
    }
}
