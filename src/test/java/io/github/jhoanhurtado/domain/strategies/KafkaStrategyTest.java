package io.github.jhoanhurtado.domain.strategies;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.Before;
import org.mockito.ArgumentCaptor;



import io.github.jhoanhurtado.domain.interfaces.MessageModel;

public class KafkaStrategyTest {
    private KafkaProducer<String, String> producerMock;
    private KafkaStrategy kafkaStrategy;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        producerMock = mock(KafkaProducer.class);
        //kafkaStrategy = new KafkaStrategy("localhost:9092") {
      //      {
      //          this.producer = producerMock;
      //      }
      //  };
    }

    @Test
    public void testSendMessage() {
        //String topic = "test-topic";
        //MessageModel message = mock(MessageModel.class);
        //when(message.getDestination()).thenReturn("destination");
        //when(message.getContent()).thenReturn("content");

        //kafkaStrategy.sendMessage(topic, message, 0, 0);

        //@SuppressWarnings("unchecked")
        //ArgumentCaptor<ProducerRecord<String, String>> captor = ArgumentCaptor.forClass(ProducerRecord.class);
        //verify(producerMock).send(captor.capture());

        //ProducerRecord<String, String> record = captor.getValue();
        //assertEquals(topic, record.topic());
        //assertEquals("destination", record.key());
        //assertEquals("content", record.value());
    }
}
