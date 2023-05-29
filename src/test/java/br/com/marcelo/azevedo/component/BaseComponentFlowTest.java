package br.com.marcelo.azevedo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseComponentFlowTest {

    @Autowired
    protected MockMvc mockMvc;

    protected String createJsonStringFrom(Object request) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(request);
    }

}
