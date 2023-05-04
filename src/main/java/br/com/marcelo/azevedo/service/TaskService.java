package br.com.marcelo.azevedo.service;

import br.com.marcelo.azevedo.controller.exchange.ListTaskThatFinishInResponse;
import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.controller.exchange.TaskResponse;
import br.com.marcelo.azevedo.entity.TaskEntity;
import br.com.marcelo.azevedo.entity.UserEntity;
import br.com.marcelo.azevedo.repository.TaskRepository;
import br.com.marcelo.azevedo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskResponse create(TaskRequest taskRequest) {
        final Optional<UserEntity> userRequesting = getUserRequesting();
        if(userRequesting.isPresent()) {
            final var taskEntity = new TaskEntity(
                    null,
                    taskRequest.name(),
                    taskRequest.description(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    LocalDate.parse(taskRequest.isToFinishAt(), formatter).atStartOfDay(),
                    userRequesting.get().getId(),
                    Boolean.FALSE,
                    null
            );
            final var newTaskEntity = taskRepository.save(taskEntity);
            return mapper(newTaskEntity);
        }

        return null; // TODO: is to throw a exception here
    }

    private Optional<UserEntity> getUserRequesting() {
        final var username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUsername();
        return userRepository.findByUsername(username);
    }

    public ListTaskThatFinishInResponse listThatFinishIn(String date) {
        final var allTasks = taskRepository.findAll();
        final var userRequesting = getUserRequesting();
        if(userRequesting.isPresent()) {

            final var tasksToDate = StreamSupport.stream(allTasks.spliterator(), true)
                    .filter(task ->
                            task.getIsToFinishAt().format(formatter).equals(date)
                                    && task.getBelongsToUserId().equals(userRequesting.get().getId()))
                    .map(this::mapper).toList();

            return new ListTaskThatFinishInResponse(date, tasksToDate);
        }
        return null; // TODO: is to throw a exception here
    }

    private TaskResponse mapper(TaskEntity taskEntity) {
        return new TaskResponse(
                taskEntity.getId(),
                taskEntity.getName(),
                taskEntity.getDescription(),
                formatter.format(taskEntity.getIsToFinishAt())
        );
    }

}
