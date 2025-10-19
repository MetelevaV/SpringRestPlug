package org.example.kafkaplug.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topics.output}")
    private String outputTopic;

    public void send(String message) {
        kafkaTemplate.send(outputTopic, message);
        log.info("Отправлено сообщение в {}: {}", outputTopic, message);
    }
}
