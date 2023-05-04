package br.com.marcelo.azevedo.facade;

import br.com.marcelo.azevedo.controller.exchange.GenerateJwtRequest;
import br.com.marcelo.azevedo.controller.exchange.JwtGeneratedResponse;
import br.com.marcelo.azevedo.service.JwtService;
import br.com.marcelo.azevedo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtFacade {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    public JwtGeneratedResponse execute(GenerateJwtRequest generateJwtRequest) {
        return jwtService.generateAccessToken(generateJwtRequest);
    }

}
