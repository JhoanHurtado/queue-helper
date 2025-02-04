package io.github.jhoanhurtado.domain.strategies;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;

/**
 * Implementación de la estrategia de mensajería utilizando RabbitMQ. Esta clase
 * permite enviar mensajes a una cola de RabbitMQ siguiendo el patrón de
 * estrategia (Strategy Pattern).
 *
 * Parámetros: - connection (Connection): Conexión activa a un servidor RabbitMQ
 * utilizada para la comunicación.
 */
public class RabbitMQStrategy implements MessagingStrategy {

    private final Connection connection;

    /**
     * Constructor de la clase RabbitMQStrategy.
     *
     * @param connection Conexión activa a RabbitMQ que se utilizará para enviar
     * mensajes.
     */
    public RabbitMQStrategy(Connection connection) {
        this.connection = connection;
    }

    /**
     * Envía un mensaje a una cola específica en RabbitMQ.
     *
     * @param queue Nombre de la cola a la que se enviará el mensaje.
     * @param message Objeto MessageModel que contiene el contenido del mensaje.
     * @param priority Prioridad del mensaje (mínimo 1 maximo 255) establece la
     * prioridad con la que se debe procesar el mensaje.
     * @param deliveryMode Modo de entrega del mensaje 1 🔹 No persistente (se
     * pierde si el servidor RabbitMQ se reinicia) 2🔹 Persistente (se almacena
     * en disco y sobrevive a reinicios)
     *
     * @throws RuntimeException Si ocurre un error al enviar el mensaje.
     */
    @Override
    public void sendMessage(String queue, MessageModel message, int priority, int deliveryMode) {
        try (Channel channel = connection.createChannel()) { 
            // Declarar la cola si no existe
            channel.queueDeclare(queue, true, false, false, null);

            /// Crear las propiedades del mensaje 
            /// deliveryMode: 1 = no persistente, 2 = persistente
            /// priority: 1 a 255 (225 es la prioridad más baja, 1 es la más alta)
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(deliveryMode < 1 ? 1 : deliveryMode)
                    .priority(priority < 1 ? 1 : priority) 
                    .build();

            // Convertir el mensaje a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messagesMap = new HashMap<>();
            messagesMap.put("content",message.toJson());
            messagesMap.put("sender", "application");

            String jsonMessage = objectMapper.writeValueAsString(messagesMap);
            
            // Publicar el mensaje en la cola especificada
            channel.basicPublish("", queue, properties, jsonMessage.getBytes());

        } catch (Exception e) {
            System.err.println("Error enviando mensaje a RabbitMQ: " + e.getMessage());
        }
    }

    @Override
    public void readMessage(String queue, MessageModel message) {
        throw new UnsupportedOperationException("Unimplemented method 'readMessage'");
    }
}
