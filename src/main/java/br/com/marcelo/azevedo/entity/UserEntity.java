package br.com.marcelo.azevedo.entity;

import br.com.marcelo.azevedo.util.LocalDateTimeDynamoDBConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.LocalDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "user")
public class UserEntity {
        @DynamoDBHashKey
        private String id;
        private String username;
        private String password;
        @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDBConverter.class)
        @DynamoDBAttribute(attributeName = "created-at")
        private LocalDateTime createdAt;
        @DynamoDBTypeConverted(converter = LocalDateTimeDynamoDBConverter.class)
        @DynamoDBAttribute(attributeName = "updated-at")
        private LocalDateTime updatedAt;
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
        @DynamoDBAttribute(attributeName = "is-active")
        private Boolean isActive;

        public UserEntity(
                String id,
                String username,
                String password,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                Boolean isActive
        ) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
                this.isActive = isActive;
        }

        public UserEntity() {}

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
        }

        public Boolean getActive() {
                return isActive;
        }

        public void setActive(Boolean active) {
                isActive = active;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                UserEntity that = (UserEntity) o;
                return id.equals(that.id);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id);
        }
}
