package br.com.marcelo.azevedo.component;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static br.com.marcelo.azevedo.fixture.UserEntityFixture.generateUserEntityFixture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserFlowTest extends BaseComponentFlowTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String newUserUsername = "marceloazevedo";
    private final String newUserPassword = "12345678";

    private String createGenericUsernameAndPasswordRequestBody() throws Exception {
        return createAUsernameAndPasswordRequestBody(newUserUsername, newUserPassword);
    }

    private String createAUsernameAndPasswordRequestBody(String username, String password) throws JsonProcessingException {
        return createJsonStringFrom(new CreateUserRequest(username, password));
    }

    @Test
    public void shouldCreateAUserWithSuccess() throws Exception {
        final var newUserEncryptedPassword = bCryptPasswordEncoder.encode(newUserPassword);

        given(userRepository.findByUsername(newUserUsername)).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(generateUserEntityFixture(newUserUsername, newUserEncryptedPassword));

        final var usernameAndPasswordRequestBody = createGenericUsernameAndPasswordRequestBody();

        this.mockMvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usernameAndPasswordRequestBody)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty());
    }

    @Test
    public void shouldReturnAErrorWhenTryToCreateAUserWhoAlreadyExists() throws Exception {
        final var newUserEncryptedPassword = bCryptPasswordEncoder.encode("another_password");

        final var userInDatabase = generateUserEntityFixture(newUserUsername, newUserEncryptedPassword);

        given(userRepository.findByUsername(newUserUsername)).willReturn(Optional.of(userInDatabase));

        final var usernameAndPasswordRequestBody = createGenericUsernameAndPasswordRequestBody();

        this.mockMvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usernameAndPasswordRequestBody)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAuthenticateAUserWithSuccess() throws Exception {
        final var newUserEncryptedPassword = bCryptPasswordEncoder.encode(newUserPassword);

        final var userInDatabase = generateUserEntityFixture(newUserUsername, newUserEncryptedPassword);

        given(userRepository.findByUsername(newUserUsername)).willReturn(Optional.of(userInDatabase));

        final var usernameAndPasswordRequestBody = createGenericUsernameAndPasswordRequestBody();

        this.mockMvc
                .perform(
                        post("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usernameAndPasswordRequestBody)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").isNotEmpty());
    }

    @Test
    public void shouldReturnAErroWhenTryToAuthenticateAUserNotFoundInDatabase() throws Exception {
        given(userRepository.findByUsername(newUserUsername)).willReturn(Optional.empty());

        final var usernameAndPasswordRequestBody = createGenericUsernameAndPasswordRequestBody();

        this.mockMvc
                .perform(
                        post("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usernameAndPasswordRequestBody)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnAErroWhenTryToAuthenticateAUserWhoHasAInvalidPassword() throws Exception {
        final var newUserEncryptedPassword = bCryptPasswordEncoder.encode(newUserPassword);

        final var userInDatabase = generateUserEntityFixture(newUserUsername, newUserEncryptedPassword);

        given(userRepository.findByUsername(newUserUsername)).willReturn(Optional.of(userInDatabase));

        final var usernameAndPasswordRequestBody = createAUsernameAndPasswordRequestBody(newUserUsername, "invalid_password");

        this.mockMvc
                .perform(
                        post("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usernameAndPasswordRequestBody)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
