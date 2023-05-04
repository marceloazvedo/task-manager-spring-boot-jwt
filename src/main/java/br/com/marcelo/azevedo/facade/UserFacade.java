package br.com.marcelo.azevedo.facade;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import br.com.marcelo.azevedo.controller.exchange.UserCreatedResponse;
import br.com.marcelo.azevedo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    @Autowired
    private UserService userService;

    public UserCreatedResponse execute(CreateUserRequest createUserRequest) {
        final var newUser = userService.saveNewUser(createUserRequest);
        return new UserCreatedResponse(newUser.getId(), newUser.getUsername());
    }

}
