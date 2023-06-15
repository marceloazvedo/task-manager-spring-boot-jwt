package br.com.marcelo.azevedo.unit;


import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.exception.TaskNotFoundException;
import br.com.marcelo.azevedo.repository.TaskRepository;
import br.com.marcelo.azevedo.service.TaskService;
import br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.marcelo.azevedo.fixture.TaskEntityFixture.generateTaskEntityFixture;
import static br.com.marcelo.azevedo.fixture.UserEntityFixture.generateUserEntityFixture;
import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateUserId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class TaskServiceTest {

    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void shouldCreateATaskWithSuccess() {
        final var userRequesting = generateUserEntityFixture(null, null);

        final var taskRequest = new TaskRequest(
                "Test 001",
                "This a description test",
                "2023-06-07"
        );

        final var taskEntity = generateTaskEntityFixture(
                taskRequest.name(),
                taskRequest.description(),
                taskRequest.isToFinishAt(),
                userRequesting.getId()
        );

        when(taskRepository.save(any())).thenReturn(taskEntity);

        final var taskCreated = taskService.create(taskRequest, userRequesting);
        assertEquals(taskCreated.getName(), taskRequest.name());
        assertEquals(taskCreated.getDescription(), taskRequest.description());
        assertEquals(taskCreated.getBelongsToUserId(), userRequesting.getId());

        verify(taskRepository, times(1)).save(any());
    }

    @Test
    public void shouldFilterTasksThatFinishInADate() {
        final var allTasks = List.of(
                generateTaskEntityFixture("task_00", "task_description_00", "1995-01-05", "user_id_test"),
                generateTaskEntityFixture("task_01", "task_description_01", "2023-06-09", "user_id_test"),
                generateTaskEntityFixture("task_02", "task_description_02", "2023-06-10", "user_id_test"),
                generateTaskEntityFixture("task_03", "task_description_03", "2023-06-11", "user_id_test"),
                generateTaskEntityFixture("task_03", "task_description_03", "2023-06-12", "user_id_test"),
                generateTaskEntityFixture("task_05", "task_description_04", "2023-06-13", "user_id_test"),
                generateTaskEntityFixture("task_05", "task_description_04", "2099-03-15", "user_id_test")
        );

        final var countOfTasksThatFinishiesInDate = 1;

        final var allTasksThatFinshInDate = taskService.listThatFinishIn(allTasks, "2023-06-12");
        assertEquals(countOfTasksThatFinishiesInDate, allTasksThatFinshInDate.size());
    }

    @Test
    public void shouldThrowsAExceptionWhenTryToFindTasksThatFinishInDate() {
        final var allTasks = List.of(
                generateTaskEntityFixture("task_00", "task_description_00", "1995-01-05", "user_id_test"),
                generateTaskEntityFixture("task_01", "task_description_01", "2023-06-09", "user_id_test"),
                generateTaskEntityFixture("task_02", "task_description_02", "2023-06-10", "user_id_test"),
                generateTaskEntityFixture("task_03", "task_description_03", "2023-06-11", "user_id_test"),
                generateTaskEntityFixture("task_05", "task_description_04", "2023-06-13", "user_id_test"),
                generateTaskEntityFixture("task_05", "task_description_04", "2099-03-15", "user_id_test")
        );

        assertThrows(TaskNotFoundException.class, () -> taskService.listThatFinishIn(allTasks, "2023-06-12"));
    }

    @Test
    public void shouldThrowsAExceptionWhenTryToFindTasksThatFinishInDateInAEmptyList() {
        assertThrows(TaskNotFoundException.class, () -> taskService.listThatFinishIn(List.of(), "2023-06-12"));
    }

    @Test
    public void shouldFindATaskForAUserWithSuccess() {
        final var taskInDatabase = generateTaskEntityFixture();

        final var taskId = taskInDatabase.getId();
        final var userOwnerId = taskInDatabase.getBelongsToUserId();

        when(taskRepository.findByIdAndBelongsToUserId(taskId, userOwnerId))
                .thenReturn(Optional.of(taskInDatabase));

        final var taskFinded = taskService.findByIdAndUserOwner(taskId, userOwnerId);
        assertEquals(taskId, taskFinded.getId());
        assertEquals(userOwnerId, taskFinded.getBelongsToUserId());
        assertEquals(taskInDatabase.getName(), taskFinded.getName());
        assertEquals(taskInDatabase.getDescription(), taskFinded.getDescription());
        assertEquals(taskInDatabase.getFinished(), taskFinded.getFinished());
        assertEquals(taskInDatabase.getCreatedAt(), taskFinded.getCreatedAt());
        assertEquals(taskInDatabase.getUpdatedAt(), taskFinded.getUpdatedAt());
        assertEquals(taskInDatabase.getFinishedAt(), taskFinded.getFinishedAt());
        assertEquals(taskInDatabase.getIsToFinishAt(), taskFinded.getIsToFinishAt());

        verify(taskRepository, times(1)).findByIdAndBelongsToUserId(taskId, userOwnerId);
    }

    @Test
    public void shouldThrowsExceptionWhenNoOneTaskWasFind() {
        final var taskInDatabase = generateTaskEntityFixture();

        final var taskId = taskInDatabase.getId();
        final var userOwnerId = taskInDatabase.getBelongsToUserId();

        when(taskRepository.findByIdAndBelongsToUserId(taskId, userOwnerId))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findByIdAndUserOwner(taskId, userOwnerId));

        verify(taskRepository, times(1)).findByIdAndBelongsToUserId(taskId, userOwnerId);
    }

    @Test
    public void shouldMarkATaskAsFinishedWithSuccess() {
        final var taskToMarkAsFinished = generateTaskEntityFixture();

        assertEquals(false, taskToMarkAsFinished.getFinished());

        when(taskRepository.save(any())).thenReturn(taskToMarkAsFinished);

        taskService.markAsFinished(taskToMarkAsFinished);

        verify(taskRepository, times(1)).save(taskToMarkAsFinished);
    }

    @Test
    public void shouldFindAllTasksForAUserWithSuccess() {
        final var userOwnerId = generateUserId();

        final var tasksInDatabaseForUser = Stream.of(
                generateTaskEntityFixture(),
                generateTaskEntityFixture(),
                generateTaskEntityFixture(),
                generateTaskEntityFixture(),
                generateTaskEntityFixture()
        ).peek(task -> task.setBelongsToUserId(userOwnerId)).toList();

        when(taskRepository.findAllByBelongsToUserId(userOwnerId)).thenReturn(tasksInDatabaseForUser);

        final var allTasksFindedForThisUser = taskService.findAllByUserIdOwner(userOwnerId);

        assertEquals(tasksInDatabaseForUser.size(), tasksInDatabaseForUser.size());

        verify(taskRepository, times(1)).findAllByBelongsToUserId(userOwnerId);
    }

}