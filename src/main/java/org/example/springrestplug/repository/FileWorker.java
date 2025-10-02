package org.example.springrestplug.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springrestplug.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Service
public class FileWorker {
    @Value("${app.file.path}")
    private String filePath;
    @Value("classpath:randomUsers.txt")
    private Resource resource;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    public FileWorker() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void writeUserToFile(User user) throws IOException {
        try (FileWriter fw = new FileWriter(filePath, true);) {
            fw.write(objectMapper.writeValueAsString(user));
            fw.write(System.lineSeparator());
        }
    }

    public String readRandomFromFile() throws IOException {
        int targetLine = random.nextInt(10);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream()))) {

            for (int i = 0; i <= targetLine; i++) {
                String line = reader.readLine();
                if (i == targetLine) {
                    return objectMapper.writeValueAsString(line);
                }
            }
        }
        throw new IOException("Файл пустой или произошла ошибка при чтении");
    }
}

