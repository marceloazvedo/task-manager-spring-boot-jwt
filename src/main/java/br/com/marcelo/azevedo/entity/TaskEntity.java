package br.com.marcelo.azevedo.entity;

import br.com.marcelo.azevedo.util.LocalDateTimeDynamoDBConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.LocalDateTime;

@DynamoDBTable(tableName = "task")
public class TaskEntity {

        @DynamoDBHashKey
        @DynamoDBAutoGeneratedKey
        private String id;
        private String name;
        private String description;
        @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDBConverter.class)
        @DynamoDBAttribute(attributeName = "created-at")
        private LocalDateTime createdAt;
        @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDBConverter.class)
        @DynamoDBAttribute(attributeName = "updated-at")
        private LocalDateTime updatedAt;
        @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDBConverter.class)
        @DynamoDBAttribute(attributeName = "is-to-finish-at")
        private LocalDateTime isToFinishAt;
        @DynamoDBAttribute(attributeName = "belongs-to-user-id")
        private String belongsToUserId;
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
        @DynamoDBAttribute(attributeName = "is-finished")
        private Boolean isFinished;
        @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDBConverter.class)
        @DynamoDBAttribute(attributeName = "finished-at")
        private LocalDateTime finishedAt;

        public TaskEntity(
                String id,
                String name,
                String description,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                LocalDateTime isToFinishAt,
                String belongsToUserId,
                Boolean isFinished,
                LocalDateTime finishedAt
        ) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
                this.isToFinishAt = isToFinishAt;
                this.belongsToUserId = belongsToUserId;
                this.isFinished = isFinished;
                this.finishedAt = finishedAt;
        }

        public String getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
                return updatedAt;
        }

        public LocalDateTime getIsToFinishAt() {
                return isToFinishAt;
        }

        public String getBelongsToUserId() {
                return belongsToUserId;
        }

        public Boolean getFinished() {
                return isFinished;
        }

        public LocalDateTime getFinishedAt() {
                return finishedAt;
        }
}
