package io.github.jhoanhurtado.infrastructure.config;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import io.github.jhoanhurtado.domain.strategies.LoggerFactory;
import io.github.jhoanhurtado.enums.LogLevel;
import io.github.jhoanhurtado.interfaces.Logger;

/**
 * Singleton para manejar conexiones con RabbitMQ. Implementado con `enum` para
 * garantizar seguridad en múltiples hilos y evitar problemas de serialización.
 */
public enum MessagingConnection {
    INSTANCE;

    /**
     * Mapa para almacenar conexiones a diferentes servidores RabbitMQ.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<String, Connection> rabbitConnections = new HashMap<>();
    Logger compositeLogger = new LoggerFactory().getCompositeLogger();

    /**
     * Obtiene una conexión a RabbitMQ para un host específico. Si la conexión
     * no existe, se crea y se almacena en caché.
     *
     * @param host Dirección del servidor RabbitMQ.
     * @param username Nombre de usuario para la autenticación.
     * @param password Contraseña para la autenticación.
     * @return Objeto {@link Connection} conectado al servidor especificado.
     * @throws RuntimeException Si ocurre un error al establecer la conexión.
     */
    public Connection getRabbitMQConnection(String host, String username, String password) {
        return rabbitConnections.computeIfAbsent(host, h -> {
            try {
                // Crear la conexión a RabbitMQ
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(h);
                factory.setUsername(username);
                factory.setPassword(password);
                Connection connection = factory.newConnection();

                // Log de éxito en la conexión
                String logMessage = String.format("Conexión establecida con RabbitMQ en el host: %s, con usuario: %s - Hora: %s",
                        host, username, getFormattedCurrentTime());
                compositeLogger.log(logMessage, LogLevel.INFO);
                return connection;
            } catch (IOException | TimeoutException e) {
                // Log de error al intentar conectar
                String errorMessage = String.format("Error al conectar con RabbitMQ en el host: %s. Error: %s - Hora: %s",
                        host, e.getMessage(), getFormattedCurrentTime());
                compositeLogger.log(errorMessage, LogLevel.CRITICAL);
            }
            return null;
        });
    }

    /**
     * Cierra todas las conexiones activas con RabbitMQ. Se debe llamar al
     * finalizar el uso de la clase para liberar recursos.
     */
    public void closeAllConnections() {
        for (Connection connection : rabbitConnections.values()) {
            try {
                if (connection.isOpen()) {
                    connection.close();

                    // Log de cierre exitoso
                    String logMessage = String.format("Conexión con RabbitMQ cerrada exitosamente - Hora: %s", getFormattedCurrentTime());
                    compositeLogger.log(logMessage, LogLevel.INFO);
                }
            } catch (IOException e) {
                // Log de error al cerrar la conexión
                String errorMessage = String.format("Error cerrando la conexión con RabbitMQ. Error: %s - Hora: %s",
                        e.getMessage(), getFormattedCurrentTime());
                compositeLogger.log(errorMessage, LogLevel.CRITICAL);
            }
        }
        rabbitConnections.clear();

        // Log de limpieza de las conexiones
        String logMessage = String.format("Todas las conexiones con RabbitMQ han sido cerradas y el mapa de conexiones ha sido limpiado - Hora: %s",
                getFormattedCurrentTime());
        compositeLogger.log(logMessage, LogLevel.INFO);
    }

    /**
     * Obtiene la hora actual formateada en hora, minutos y segundos, y día.
     *
     * @return Hora formateada en el formato "HH:mm:ss dd/MM/yyyy"
     */
    private String getFormattedCurrentTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        return now.format(DATE_TIME_FORMATTER);
    }
}
