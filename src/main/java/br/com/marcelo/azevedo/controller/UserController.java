package br.com.marcelo.azevedo.controller;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import br.com.marcelo.azevedo.controller.exchange.GenerateJwtRequest;
import br.com.marcelo.azevedo.controller.exchange.JwtGeneratedResponse;
import br.com.marcelo.azevedo.controller.exchange.UserCreatedResponse;
import br.com.marcelo.azevedo.facade.UserFacade;
import br.com.marcelo.azevedo.facade.JwtFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserFacade userFacade;

    private final JwtFacade jwtFacade;

    public UserController(UserFacade userFacade, JwtFacade jwtFacade) {
        this.userFacade = userFacade;
        this.jwtFacade = jwtFacade;
    }

    @PostMapping
    public ResponseEntity<UserCreatedResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return new ResponseEntity<>(userFacade.execute(createUserRequest), HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/authenticate")
    public ResponseEntity<JwtGeneratedResponse> createAuthentication(@RequestBody GenerateJwtRequest generateJwtRequest) {
        return new ResponseEntity<>(jwtFacade.execute(generateJwtRequest), HttpStatus.CREATED);
    }

}
