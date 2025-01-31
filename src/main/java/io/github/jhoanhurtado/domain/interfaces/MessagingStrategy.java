package io.github.jhoanhurtado.domain.interfaces;

/**
 * Interfaz que define la estrategia de mensajería para el envío de mensajes.
 * Permite implementar diferentes mecanismos de envío, como RabbitMQ, Kafka, etc.
 */
public interface MessagingStrategy {

    /**
     * Envía un mensaje a una cola específica.
     *
     * @param queue        Nombre de la cola a la que se enviará el mensaje.
     * @param message      Objeto MessageModel que contiene el contenido del mensaje.
     * @param priority     Prioridad del mensaje (valores mínimos deben ser 1 para evitar errores).
     * @param deliveryMode Modo de entrega del mensaje (valores mínimos deben ser 1 para evitar errores).
     */
    void sendMessage(String queue, MessageModel message, int priority, int deliveryMode);
}
