package br.com.marcelo.azevedo.controller;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import br.com.marcelo.azevedo.controller.exchange.GenerateJwtRequest;
import br.com.marcelo.azevedo.controller.exchange.JwtGeneratedResponse;
import br.com.marcelo.azevedo.controller.exchange.UserCreatedResponse;
import br.com.marcelo.azevedo.facade.UserFacade;
import br.com.marcelo.azevedo.facade.JwtFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private JwtFacade jwtFacade;

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
