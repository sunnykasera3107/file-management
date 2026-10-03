package com.manager.users.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.manager.users.kafka.dto.LogProducer;

import tools.jackson.databind.annotation.JsonSerialize;

@Configuration 
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

    @Bean 
    public ProducerFactory<String, LogProducer> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            kafkaServer
        );

        config.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class
        );

        config.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            JsonSerialize.class
        );

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, LogProducer> kafkaTemplate(
            ProducerFactory<String, LogProducer> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}