package io.github.jhoanhurtado.domain.strategies;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Implementación de la estrategia de mensajería utilizando RabbitMQ.
 * Esta clase permite enviar mensajes a una cola de RabbitMQ siguiendo el 
 * patrón de estrategia (Strategy Pattern).
 *
 * Parámetros:
 * - connection (Connection): 
 *   Conexión activa a un servidor RabbitMQ utilizada para la comunicación.
 */
public class RabbitMQStrategy implements MessagingStrategy {
    
    private Connection connection;

    /**
     * Constructor de la clase RabbitMQStrategy.
     *
     * @param connection Conexión activa a RabbitMQ que se utilizará para enviar mensajes.
     */
    public RabbitMQStrategy(Connection connection) {
        this.connection = connection;
    }

    /**
     * Envía un mensaje a una cola específica en RabbitMQ.
     *
     * @param queue       Nombre de la cola a la que se enviará el mensaje.
     * @param message     Objeto MessageModel que contiene el contenido del mensaje.
     * @param priority    Prioridad del mensaje (mínimo 1 maximo 255) establece la prioridad con la que se debe procesar el mensaje.
     * @param deliveryMode Modo de entrega del mensaje 1 🔹 No persistente (se pierde si el servidor RabbitMQ se reinicia) 2🔹 Persistente (se almacena en disco y sobrevive a reinicios)
     * 
     * @throws RuntimeException Si ocurre un error al enviar el mensaje.
     */
    @Override
    public void sendMessage(String queue, MessageModel message, int priority, int deliveryMode) {
        try (Channel channel = connection.createChannel()) { // Utilizamos try-with-resources para asegurar el cierre del canal
            // Declarar la cola si no existe
            channel.queueDeclare(queue, true, false, false, null);

            // Configurar las propiedades del mensaje
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(deliveryMode < 1 ? 1 : deliveryMode) // Garantiza que deliveryMode sea al menos 1
                    .priority(priority < 1 ? 1 : priority) // Garantiza que priority sea al menos 1
                    .build();

            // Publicar el mensaje en la cola especificada
            channel.basicPublish("", queue, properties, message.getContent().getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Error enviando mensaje a RabbitMQ", e);
        }
    }
}