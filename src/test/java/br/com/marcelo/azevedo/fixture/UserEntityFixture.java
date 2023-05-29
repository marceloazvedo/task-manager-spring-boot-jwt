package br.com.marcelo.azevedo.fixture;

import br.com.marcelo.azevedo.entity.UserEntity;

import java.time.LocalDateTime;

import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateUserId;

public class UserEntityFixture {

    public static UserEntity generateUserEntityFixture(
            String username,
            String password
    ) {
        return new UserEntity(
                generateUserId(),
                null == username ? "test_user" : username,
                null == password ? "test_password" : password,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Boolean.TRUE
        );
    }

}
