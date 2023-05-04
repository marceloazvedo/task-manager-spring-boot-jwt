package br.com.marcelo.azevedo.service;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import br.com.marcelo.azevedo.domain.ApplicationUserContext;
import br.com.marcelo.azevedo.entity.UserEntity;
import br.com.marcelo.azevedo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity saveNewUser(CreateUserRequest createUserRequest) {
        final var hasUserWithThisUsername = userRepository.findByUsername(createUserRequest.username()).isPresent();

        if (hasUserWithThisUsername) {
            throw new RuntimeException();
        }

        return userRepository.save(
                new UserEntity(
                        null,
                        createUserRequest.username(),
                        bCryptPasswordEncoder.encode(createUserRequest.password()),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Boolean.TRUE
                )
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(userFound -> new ApplicationUserContext(userFound.getId(), userFound.getUsername(), userFound.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
    }
}
