package io.github.jhoanhurtado.domain.strategies;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;
import io.github.jhoanhurtado.enums.LogLevel;
import io.github.jhoanhurtado.interfaces.Logger;

/**
 * Implementación de la estrategia de mensajería utilizando RabbitMQ. Esta clase
 * permite enviar mensajes a una cola de RabbitMQ siguiendo el patrón de
 * estrategia (Strategy Pattern).
 */
public class RabbitMQStrategy implements MessagingStrategy {

    private final Connection connection;
    private final Logger compositeLogger;
    /**
     * Constructor de la clase RabbitMQStrategy.
     *
     * @param connection Conexión activa a RabbitMQ que se utilizará para enviar
     *                  mensajes.
     */
    public RabbitMQStrategy(Connection connection) {
        this.connection = connection;
        this.compositeLogger = new LoggerFactory().getCompositeLogger();
    }

    /**
     * Envía un mensaje a una cola específica en RabbitMQ.
     *
     * @param queue      Nombre de la cola a la que se enviará el mensaje.
     * @param message    Objeto MessageModel que contiene el contenido del mensaje.
     * @param priority   Prioridad del mensaje (mínimo 1 máximo 255) establece la
     *                   prioridad con la que se debe procesar el mensaje.
     * @param deliveryMode Modo de entrega del mensaje: 1 🔹 No persistente (se
     *                     pierde si el servidor RabbitMQ se reinicia), 2 🔹
     *                     Persistente (se almacena en disco y sobrevive a reinicios)
     * @throws RuntimeException Si ocurre un error al enviar el mensaje.
     */
    @Override
    public void sendMessage(String queue, MessageModel message, int priority, int deliveryMode) {
        String startLogMessage = String.format("Iniciando envío de mensaje a RabbitMQ. Cola: %s, Prioridad: %d, Modo de entrega: %d", queue, priority, deliveryMode);
        compositeLogger.log(startLogMessage, LogLevel.INFO);

        try (Channel channel = connection.createChannel()) {
            // Declarar la cola si no existe
            channel.queueDeclare(queue, true, false, false, null);
            compositeLogger.log("Cola declarada exitosamente: " + queue, LogLevel.INFO);

            // Crear las propiedades del mensaje
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(deliveryMode < 1 ? 1 : deliveryMode)
                    .priority(priority < 1 ? 1 : priority)
                    .build();

            // Convertir el mensaje a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messagesMap = new HashMap<>();
            messagesMap.put("content", message.toJson());
            messagesMap.put("sender", "application");

            String jsonMessage = objectMapper.writeValueAsString(messagesMap);
            compositeLogger.log("Mensaje convertido a JSON: " + jsonMessage, LogLevel.DEBUG);

            // Publicar el mensaje en la cola especificada
            channel.basicPublish("", queue, properties, jsonMessage.getBytes());
            String logMessageString = String.format("Mensaje enviado con éxito a la cola: %s. Contenido: %s", queue, jsonMessage);
            compositeLogger.log(logMessageString, LogLevel.INFO);

        } catch (Exception e) {
            String errorLogMessage = String.format("Error enviando mensaje a RabbitMQ. Cola: %s, Error: %s", queue, e.getMessage());
            compositeLogger.log(errorLogMessage, LogLevel.CRITICAL);
        }
    }

    /**
     * Método no implementado para leer mensajes desde RabbitMQ.
     *
     * @param queue Nombre de la cola.
     * @param message Objeto que contendrá el mensaje leído.
     */
    @Override
    public void readMessage(String queue, MessageModel message) {
        String logMessage = String.format("Intento de lectura de mensaje desde la cola: %s, pero el método no está implementado.", queue);
        compositeLogger.log(logMessage, LogLevel.WARN);
    }
}