package io.github.jhoanhurtado.domain.strategies;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import io.github.jhoanhurtado.domain.models.MessageModelRequest;
import io.github.jhoanhurtado.observer.MessageObserver;

/**
 * Consumer de RabbitMQ que usa el patrón Observer para notificar los mensajes.
 */
public class RabbitMQConsumer {

    private static final String QUEUE_NAME = "queue-email-message-sending";
    private final MessageObserver observer;
    private final Connection connection;

    /**
     * Construye un nuevo RabbitMQConsumer con el MessageObserver y la Connection especificados.
     *
     * @param observer el MessageObserver que manejará los mensajes consumidos de la cola
     * @param connection la Connection al servidor RabbitMQ
     */
    public RabbitMQConsumer(MessageObserver observer, Connection connection) {
        this.observer = observer;
        this.connection = connection;
    }

    /**
     * Inicia la escucha de mensajes en la cola de RabbitMQ.
     */
    /**
     * Inicia la escucha de la cola de RabbitMQ especificada por QUEUE_NAME.
     * 
     * <p>Este método crea un canal y declara la cola. Configura un DeliverCallback 
     * para manejar los mensajes entrantes. Los mensajes se convierten de JSON a un 
     * objeto MessageModelRequest y luego se notifican a los observadores.
     * 
     * <p>Si hay un error al conectar con RabbitMQ, se lanza una RuntimeException.
     * 
     * @throws RuntimeException si hay un error al conectar con RabbitMQ
     */
    public void startListening() {

        try (Channel channel = connection.createChannel()) {
            // Declarar la cola
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Crear un DeliverCallback para manejar los mensajes entrantes
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);

                // Convertir JSON a MessageModel (usar librería como Gson o Jackson)
                MessageModelRequest messageModel = MessageModelRequest.fromJson(jsonMessage);

                // Notificar a los observadores
                observer.notify(messageModel);
            };

            // Iniciar la escucha de mensajes
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            System.err.println("Error al conectar con RabbitMQ: " + e.getMessage());
        }
    }
}
