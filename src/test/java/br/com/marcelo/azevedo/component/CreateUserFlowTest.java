package br.com.marcelo.azevedo.component;

import br.com.marcelo.azevedo.controller.exchange.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateUserFlowTest extends BaseComponentFlowTest {

    @Test
    public void shouldCreateAuSerWithSuccess() throws Exception {
        final var createUserRequest = createJsonStringFrom(
                new CreateUserRequest(
                "marceloazevedo",
                "12345678"
                )
        );

        this.mockMvc
                .perform(
                        post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createUserRequest)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").isNotEmpty());
    }

}
