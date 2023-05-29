package br.com.marcelo.azevedo.facade;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import br.com.marcelo.azevedo.controller.exchange.UserCreatedResponse;
import br.com.marcelo.azevedo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public UserCreatedResponse execute(CreateUserRequest createUserRequest) {
        userService.validateIfHasUserWithThisUsername(createUserRequest.username());
        final var newUserCreated = userService.create(createUserRequest);
        return new UserCreatedResponse(newUserCreated.getId(), newUserCreated.getUsername());
    }

}
