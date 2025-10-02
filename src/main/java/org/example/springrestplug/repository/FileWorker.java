package org.example.springrestplug.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springrestplug.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Component
public class FileWorker {
    @Value("${app.file1.path}")
    private String file1Path;
    @Value("${app.file2.path}")
    private String file2Path;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public void writeUserToFile(User user) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS).enable(INDENT_OUTPUT);;
        objectMapper.writeValue(new File(file1Path), user);
    }

    public String readRandomFromFile() throws IOException {
        JsonNode root = objectMapper.readTree(Paths.get(file2Path).toFile());

        if (!root.isArray() || root.isEmpty()) {
            throw new IOException("Файл пуст или не содержит массив JSON");
        }
        JsonNode randomNode = root.get(random.nextInt(root.size()));

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(randomNode);
    }
}

