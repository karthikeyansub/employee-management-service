package com.bank.employee.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected byte[] readFromFile(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get("src", "test", "resources", fileName));
    }

    protected <T> T serialize(String fileName, Class<T> clazz) throws Exception {
        return objectMapper.readValue(readFromFile(fileName), clazz);
    }
}
