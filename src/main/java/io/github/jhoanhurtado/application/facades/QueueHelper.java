package io.github.jhoanhurtado.application.facades;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Connection;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;
import io.github.jhoanhurtado.domain.strategies.KafkaStrategy;
import io.github.jhoanhurtado.domain.strategies.LoggerFactory;
import io.github.jhoanhurtado.domain.strategies.RabbitMQStrategy;
import io.github.jhoanhurtado.enums.LogLevel;
import io.github.jhoanhurtado.infrastructure.config.MessagingConnection;
import io.github.jhoanhurtado.interfaces.Logger;

/**
 * Clase fachada que gestiona conexiones a diferentes brokers de mensajería,
 * incluyendo RabbitMQ y otros como Kafka.
 */
public class QueueHelper {

    private final Map<String, MessagingStrategy> strategies; // Mapa de estrategias por broker
    private final Map<String, Connection> connections; // Mapa de conexiones a los brokers
    Logger compositeLogger = new LoggerFactory().getCompositeLogger();

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
    public void withRabbitMQ(String brokerName, String host, String username, String password) {
        try {
            Connection connection = MessagingConnection.INSTANCE.getRabbitMQConnection(host, username, password);
            connections.put(brokerName, connection);

            // Asociar la estrategia de RabbitMQ a esta conexión
            strategies.put(brokerName, new RabbitMQStrategy(connection));
            
            // Log de inicio de conexión con hora y fecha
            String logMessage = String.format("Iniciando conexión a RabbitMQ broker %s en host %s - Hora: %s", 
                brokerName, host, getFormattedCurrentTime());
            compositeLogger.log(logMessage, LogLevel.INFO);
        } catch (Exception e) {
            // Log en caso de error en la conexión
            String logMessage = String.format("Error al establecer conexión a RabbitMQ broker %s. Error: %s - Hora: %s", 
                brokerName, e.getMessage(), getFormattedCurrentTime());
            compositeLogger.log(logMessage, LogLevel.CRITICAL);
            throw e; // Propagar la excepción
        }
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
        
        // Log de configuración de Kafka
        String logMessage = String.format("Configurando conexión con Kafka broker %s para cola %s - Hora: %s", 
            broker, queue, getFormattedCurrentTime());
        helper.compositeLogger.log(logMessage, LogLevel.INFO);
        
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
                
                // Log de desconexión
                String logMessage = String.format("Desconectando broker %s - Hora: %s", brokerName, getFormattedCurrentTime());
                compositeLogger.log(logMessage, LogLevel.INFO);
            }
        } catch (IOException e) {
            // Log en caso de error al desconectar
            String logMessage = String.format("Error al desconectar broker %s. Error: %s - Hora: %s", 
                brokerName, e.getMessage(), getFormattedCurrentTime());
            compositeLogger.log(logMessage, LogLevel.CRITICAL);
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

    /**
     * Obtiene la hora actual formateada en hora, minutos y segundos, y día.
     * 
     * @return Hora formateada en el formato "HH:mm:ss dd/MM/yyyy"
     */
    private static String getFormattedCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return sdf.format(new Date());
    }
}