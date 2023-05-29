package br.com.marcelo.azevedo.unit;


import br.com.marcelo.azevedo.controller.exchange.TaskRequest;
import br.com.marcelo.azevedo.repository.TaskRepository;
import br.com.marcelo.azevedo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static br.com.marcelo.azevedo.fixture.TaskEntityFixture.generateTaskEntityFixture;
import static br.com.marcelo.azevedo.fixture.UserEntityFixture.generateUserEntityFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

}