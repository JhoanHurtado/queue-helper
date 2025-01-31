package io.github.jhoanhurtado.application.facades;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
import io.github.jhoanhurtado.domain.interfaces.MessagingStrategy;

/**
 * Fachada para el envío de mensajes mediante una estrategia de mensajería
 * específica.
 * Esta clase proporciona una interfaz simplificada para enviar mensajes sin
 * depender
 * directamente de la implementación concreta de la estrategia de mensajería.
 */
public class MessagingFacade {

    /** Estrategia de mensajería utilizada para enviar mensajes. */
    private MessagingStrategy strategy;

    /**
     * Constructor de la clase MessagingFacade.
     *
     * @param strategy Estrategia de mensajería que se utilizará para el envío de
     *                 mensajes.
     *                 Debe ser una implementación de {@link MessagingStrategy}.
     */
    public MessagingFacade(MessagingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Envía un mensaje a una cola o un tópico utilizando la estrategia definida.
     *
     * @param queueOrTopic Nombre de la cola o tópico de destino.
     * @param message      Mensaje a enviar, representado por un objeto
     *                     {@link MessageModel}.
     * @param priority     Prioridad del mensaje (valores mínimos deben ser 1 para
     *                     evitar errores).
     * @param deliveryMode Modo de entrega del mensaje (valores mínimos deben ser 1
     *                     para evitar errores).
     */
    public void send(String queueOrTopic, MessageModel message, int priority, int deliveryMode) {
        strategy.sendMessage(queueOrTopic, message, priority, deliveryMode);
    }
}
