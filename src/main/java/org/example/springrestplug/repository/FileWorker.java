package org.example.springrestplug.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springrestplug.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class FileWorker {
    @Value("${app.file1.path}")
    private String file1Path;
    @Value("${app.file2.path}")
    private String file2Path;
    @Value("${app.file3.path}")
    private String csvFilePath;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    public FileWorker() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void writeUserToFile(User user) throws IOException {
        File file = new File(file1Path);

        List<User> users = new ArrayList<>();

        if (file.exists() && file.length() > 0) {
            users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
        }

        users.add(user);

        objectMapper.writeValue(file, users);
    }

    public void writeUserToCsv(User user) throws IOException {
        try (FileWriter writer = new FileWriter(csvFilePath, true)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String line = String.join(",",
                    user.getLogin(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getDate().format(formatter)
            );

            writer.write(line + System.lineSeparator());
        }
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

