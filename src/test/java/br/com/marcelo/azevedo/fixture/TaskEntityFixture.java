package br.com.marcelo.azevedo.fixture;

import br.com.marcelo.azevedo.entity.TaskEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.marcelo.azevedo.util.LocalDateTimeConverter.getByShortStringFormat;
import static br.com.marcelo.azevedo.util.StringConverter.getShortStringDate;
import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateTaskId;
import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateUserId;

public class TaskEntityFixture {

    public static TaskEntity generateTaskEntityFixture(
            String taskName,
            String taskDescription,
            String isToFinishAt,
            String userId
    ) {

        return new TaskEntity(
                generateTaskId(),
                null == taskName ? "Test Name Task" : taskName,
                null == taskDescription ? " Test Description Task" : taskDescription,
                LocalDateTime.now(),
                LocalDateTime.now(),
                getByShortStringFormat(null == isToFinishAt ? getShortStringDate(LocalDateTime.now()) : isToFinishAt),
                null == userId ? generateUserId() : userId,
                Boolean.FALSE,
                null
        );
    }

    public static TaskEntity generateTaskEntityFixture() {
        return generateTaskEntityFixture(null, null, null, null);
    }

}
