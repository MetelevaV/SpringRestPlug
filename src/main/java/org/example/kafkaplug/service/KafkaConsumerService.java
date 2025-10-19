package org.example.kafkaplug.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafkaplug.model.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final KafkaProducerService producerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${log.file.path}")
    private String logFilePath;

    @KafkaListener(topics = "${topics.input}", groupId = "kafka-stub-group")
    public void consume(String message) {
        try {
            writeMessageToFile(message);

            UserMessage user = objectMapper.readValue(message, UserMessage.class);

            user.setStatus("processed");
            user.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            String updatedMessage = objectMapper.writeValueAsString(user);
            producerService.send(updatedMessage);

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения", e);
        }
    }

    private void writeMessageToFile(String message) {
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " — " + message + System.lineSeparator());
        } catch (IOException e) {
            log.error("Ошибка при записи сообщения в файл", e);
        }
    }
}

