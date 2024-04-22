package com.example.prueba_tecnica.kafka;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;

public class Producer {
    @Value("${topic}")
    String topico;
    @Autowired
    KafkaTemplate<String, AuditoriaDto> kafkaTemplate;
    private KafkaProducer<String, String> producer;

    public Producer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(properties);
    }
    public void send(String nameTopic, String message) {
        producer.send(new ProducerRecord<>(nameTopic, message));
    }
    public void cerrar() {
        producer.close();
    }

}
