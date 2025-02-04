package io.github.jhoanhurtado.domain.strategies;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import io.github.jhoanhurtado.domain.models.MessageModelRequest;
import io.github.jhoanhurtado.observer.MessageObserver;

public class RabbitMQConsumerTest {

    private MessageObserver observer;
    private Connection connection;
    private Channel channel;
    private RabbitMQConsumer rabbitMQConsumer;

    @Before
    public void setUp() throws IOException {
        observer = mock(MessageObserver.class);
        connection = mock(Connection.class);
        channel = mock(Channel.class);

        when(connection.createChannel()).thenReturn(channel);
        rabbitMQConsumer = new RabbitMQConsumer(observer, connection);
    }

    @Test
    public void testStartListening() throws Exception {
        rabbitMQConsumer.startListening();

        ArgumentCaptor<DeliverCallback> deliverCallbackCaptor = ArgumentCaptor.forClass(DeliverCallback.class);
        verify(channel).basicConsume(eq("queue-email-message-sending"), eq(true), deliverCallbackCaptor.capture(), any(CancelCallback.class));

        DeliverCallback deliverCallback = deliverCallbackCaptor.getValue();
        assertNotNull("DeliverCallback no debería ser null", deliverCallback);

        String jsonMessage = "{\"message\":\"test\"}";
        byte[] messageBody = jsonMessage.getBytes(StandardCharsets.UTF_8);

        deliverCallback.handle("consumerTag", new com.rabbitmq.client.Delivery(null, null, messageBody));

        verify(observer).notify(any(MessageModelRequest.class));
    }
}