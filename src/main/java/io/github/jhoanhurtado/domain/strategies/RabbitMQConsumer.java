package io.github.jhoanhurtado.domain.strategies;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import io.github.jhoanhurtado.domain.models.MessageModelRequest;
import io.github.jhoanhurtado.enums.LogLevel;
import io.github.jhoanhurtado.interfaces.Logger;
import io.github.jhoanhurtado.observer.MessageObserver;

/**
 * Consumer de RabbitMQ que usa el patrón Observer para notificar los mensajes.
 */
public class RabbitMQConsumer {

    private final String queueName;
    private final MessageObserver observer;
    private final Connection connection;
    Logger compositeLogger = new LoggerFactory().getCompositeLogger();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Construye un nuevo RabbitMQConsumer con el MessageObserver y la
     * Connection especificados.
     *
     * @param observer el MessageObserver que manejará los mensajes consumidos
     * de la cola
     * @param connection la Connection al servidor RabbitMQ
     * @param connection nombre de la cola RabbitMQ
     */
    public RabbitMQConsumer(MessageObserver observer, Connection connection, String queue) {
        this.observer = observer;
        this.connection = connection;
        this.queueName = queue;
        compositeLogger.log(getCurrentDateTime() + " - Inicializando RabbitMQConsumer con conexión establecida", LogLevel.INFO);
    }

    /**
     * Inicia la escucha de mensajes en la cola de RabbitMQ.
     */
    /**
     * Inicia la escucha de la cola de RabbitMQ especificada por queueName.
     *
     * <p>
     * Este método crea un canal y declara la cola. Configura un DeliverCallback
     * para manejar los mensajes entrantes. Los mensajes se convierten de JSON a
     * un objeto MessageModelRequest y luego se notifican a los observadores.
     *
     * <p>
     * Si hay un error al conectar con RabbitMQ, se lanza una RuntimeException.
     *
     * @throws RuntimeException si hay un error al conectar con RabbitMQ
     */
    @SuppressWarnings("squid:S2095")
    public void startListening() {
        String logMessage = getCurrentDateTime() + " - Iniciando escucha de la cola RabbitMQ: " + queueName;
        compositeLogger.log(logMessage, LogLevel.INFO);
        
        try {
            var channel = connection.createChannel();
            compositeLogger.log(getCurrentDateTime() + " - Canal RabbitMQ creado correctamente.", LogLevel.INFO);

            // Declarar la cola
            channel.queueDeclare(queueName, true, false, false, null);
            compositeLogger.log(getCurrentDateTime() + " - Cola declarada exitosamente: " + queueName, LogLevel.INFO);

            // Crear un DeliverCallback para manejar los mensajes entrantes
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                compositeLogger.log(getCurrentDateTime() + " - Mensaje recibido, procesando: " + jsonMessage, LogLevel.INFO);

                try {
                    // Convertir JSON a MessageModel (usar librería como Gson o Jackson)
                    MessageModelRequest messageModel = MessageModelRequest.fromJson(jsonMessage);
                    compositeLogger.log(getCurrentDateTime() + " - Mensaje procesado correctamente, notificando a los observadores." + messageModel.getContent(), LogLevel.INFO);
                    
                    // Notificar a los observadores
                    observer.notify(messageModel);
                } catch (Exception e) {
                    compositeLogger.log(getCurrentDateTime() + " - Error al procesar el mensaje: " + e.getMessage(), LogLevel.ERROR);
                }
            };

            // Iniciar la escucha de mensajes
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                // No se realiza ninguna acción sobre el consumerTag
            });

            compositeLogger.log(getCurrentDateTime() + " - Escuchando mensajes en la cola " + queueName + "...", LogLevel.INFO);
        } catch (IOException e) {
            String errorMessage = String.format(" %s - Ocurrió un error al consumir la cola %s: %s", getCurrentDateTime(),queueName, e.getMessage());
            compositeLogger.log(errorMessage, LogLevel.CRITICAL);
        }
    }

    /**
     * Obtiene la fecha y hora actual en la zona horaria de Colombia.
     *
     * @return la fecha y hora en formato "yyyy-MM-dd HH:mm:ss"
     */
    private String getCurrentDateTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        return now.format(DATE_TIME_FORMATTER);
    }
}