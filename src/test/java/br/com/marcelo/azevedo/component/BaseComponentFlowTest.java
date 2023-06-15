package br.com.marcelo.azevedo.component;

import br.com.marcelo.azevedo.repository.TaskRepository;
import br.com.marcelo.azevedo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class BaseComponentFlowTest {

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected TaskRepository taskRepository;

    @Autowired
    protected MockMvc mockMvc;

    protected String createJsonStringFrom(Object request) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(request);
    }

}
