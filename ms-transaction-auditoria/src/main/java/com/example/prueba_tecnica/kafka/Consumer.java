package com.example.prueba_tecnica.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class Consumer {
    private KafkaConsumer<String, String> consumer;

    public Consumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", "grupo-auditor");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(properties);
    }
    public void subscribe(String nameTopic) {
        consumer.subscribe(List.of(nameTopic));
        while (true) {
            ConsumerRecords<String, String>  consumerRecord = consumer.poll(Duration.ofMillis(200)); // pregunto si existe mensaje para ser consumido
            for (ConsumerRecord<String, String> record:consumerRecord) {
                System.out.println(record.value());
            }
        }
    }
    public void cerrar() {
        consumer.close();
    }

}
