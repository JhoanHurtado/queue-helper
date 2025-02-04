package io.github.jhoanhurtado.application.facades;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Connection;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;
import io.github.jhoanhurtado.domain.strategies.KafkaStrategy;
import io.github.jhoanhurtado.domain.strategies.RabbitMQStrategy;
import io.github.jhoanhurtado.infrastructure.config.MessagingConnection;

/**
 * Clase fachada que gestiona conexiones a diferentes brokers de mensajería,
 * incluyendo RabbitMQ y otros como Kafka.
 */
public class QueueHelper {

    private final Map<String, MessagingStrategy> strategies; // Mapa de estrategias por broker
    private final Map<String, Connection> connections; // Mapa de conexiones a los brokers

    /**
     * Constructor de la clase QueueHelper. Inicializa las conexiones y
     * estrategias.
     */
    public QueueHelper() {
        strategies = new HashMap<>();
        connections = new HashMap<>();
    }

    /**
     * Establece una conexión a RabbitMQ utilizando la clase
     * MessagingConnection.
     *
     * @param brokerName Nombre del broker (por ejemplo, "rabbitmq1").
     * @param host Dirección del servidor RabbitMQ.
     * @param username Nombre de usuario para la autenticación.
     * @param password Contraseña para la autenticación.
     * @throws Exception Si ocurre un error al establecer la conexión.
     */
    public void withRabbitMQ(String brokerName, String host, String username, String password) throws Exception {
        Connection connection = MessagingConnection.INSTANCE.getRabbitMQConnection(host, username, password);
        connections.put(brokerName, connection);

        // Asociar la estrategia de RabbitMQ a esta conexión
        strategies.put(brokerName, new RabbitMQStrategy(connection));
    }

    /**
     * Método para enviar un mensaje a un broker utilizando la estrategia
     * configurada.
     *
     * @param brokerName Nombre del broker al que se enviará el mensaje.
     * @param queue Nombre de la cola a la que se enviará el mensaje.
     * @param message Objeto MessageModel que contiene el contenido del mensaje.
     * @param priority Prioridad del mensaje.
     * @param deliveryMode Modo de entrega del mensaje.
     * @throws QueueHelper Instancia configurada con Kafka strategy.
     */
    public static QueueHelper withKafka(String broker, String queue, MessageModel message, int priority,
            int deliveryMode) {
        QueueHelper helper = new QueueHelper();
        helper.strategies.put(queue, new KafkaStrategy(broker));
        return helper;
    }

    /**
     * Método para cerrar la conexión de un broker.
     *
     * @param brokerName Nombre del broker a desconectar.
     */
    public void disconnect(String brokerName) {
        try {
            Connection connection = connections.get(brokerName);
            if (connection != null && connection.isOpen()) {
                connection.close();
                connections.remove(brokerName);
                strategies.remove(brokerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la conexión por el nombre del broker.
     *
     * @param brokerName Nombre del broker.
     * @return La conexión asociada al broker, o null si no existe.
     */
    public Connection getConnection(String brokerName) {
        return connections.get(brokerName);
    }
}
