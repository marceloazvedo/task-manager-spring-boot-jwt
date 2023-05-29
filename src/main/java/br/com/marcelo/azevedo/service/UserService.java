package br.com.marcelo.azevedo.service;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import br.com.marcelo.azevedo.domain.ApplicationUserContext;
import br.com.marcelo.azevedo.entity.UserEntity;
import br.com.marcelo.azevedo.exception.UserFoundWithThisUsernameException;
import br.com.marcelo.azevedo.exception.UserNotFoundException;
import br.com.marcelo.azevedo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static br.com.marcelo.azevedo.util.UUIDGeneratorWithPattern.generateUserId;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserEntity create(CreateUserRequest createUserRequest) {
        return userRepository.save(
                new UserEntity(
                        generateUserId(),
                        createUserRequest.username(),
                        bCryptPasswordEncoder.encode(createUserRequest.password()),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Boolean.TRUE
                )
        );
    }

    public void validateIfHasUserWithThisUsername(String username) {
        final var hasUserWithThisUsername = findUserByUsername(username).isPresent();
        if (hasUserWithThisUsername) throw new UserFoundWithThisUsernameException();
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username)
                .map(userFound -> new ApplicationUserContext(
                                userFound.getId(),
                                userFound.getUsername(),
                                userFound.getPassword(),
                                new ArrayList<>()
                        )
                )
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
    }

    public UserEntity getUserRequesting() {
        final var username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return findUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

}
