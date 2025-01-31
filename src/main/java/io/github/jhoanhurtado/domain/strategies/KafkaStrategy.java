package io.github.jhoanhurtado.domain.strategies;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;

public class KafkaStrategy implements MessagingStrategy {
    protected KafkaProducer<String, String> producer;

    public KafkaStrategy(String broker) {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void sendMessage(String topic, MessageModel message, int priority, int deliveryMode) {
        producer.send(new ProducerRecord<>(topic, message.getDestination(), message.getContent()));
    }
}
