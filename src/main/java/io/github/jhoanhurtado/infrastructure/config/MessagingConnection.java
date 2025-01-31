package io.github.jhoanhurtado.infrastructure.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Singleton para manejar conexiones con RabbitMQ.
 * Implementado con `enum` para garantizar seguridad en múltiples hilos y evitar problemas de serialización.
 */
public enum MessagingConnection {
    INSTANCE;

    /** Mapa para almacenar conexiones a diferentes servidores RabbitMQ. */
    private final Map<String, Connection> rabbitConnections = new HashMap<>();

    /**
     * Obtiene una conexión a RabbitMQ para un host específico.
     * Si la conexión no existe, se crea y se almacena en caché.
     *
     * @param host     Dirección del servidor RabbitMQ.
     * @param username Nombre de usuario para la autenticación.
     * @param password Contraseña para la autenticación.
     * @return Objeto {@link Connection} conectado al servidor especificado.
     * @throws RuntimeException Si ocurre un error al establecer la conexión.
     */
    public Connection getRabbitMQConnection(String host, String username, String password) {
        return rabbitConnections.computeIfAbsent(host, h -> {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(h);
                factory.setUsername(username);
                factory.setPassword(password);
                return factory.newConnection();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException("Error al conectar con RabbitMQ", e);
            }
        });
    }

    /**
     * Cierra todas las conexiones activas con RabbitMQ.
     * Se debe llamar al finalizar el uso de la clase para liberar recursos.
     */
    public void closeAllConnections() {
        for (Connection connection : rabbitConnections.values()) {
            try {
                if (connection.isOpen()) {
                    connection.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error cerrando la conexión con RabbitMQ", e);
            }
        }
        rabbitConnections.clear();
    }
}
