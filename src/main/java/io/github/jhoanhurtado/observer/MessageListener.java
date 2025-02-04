package io.github.jhoanhurtado.observer;

import io.github.jhoanhurtado.domain.models.MessageModelRequest;

/**
 * Interfaz funcional que define un método para manejar la recepción de mensajes.
 * 
 * <p>Esta interfaz se utiliza para implementar la lógica que se ejecutará cuando se reciba un mensaje.</p>
 * 
 * <p>El tag {@code @FunctionalInterface} indica que esta interfaz está destinada a ser una interfaz funcional, 
 * lo que significa que tiene exactamente un método abstracto.</p>
 * 
 * @param message El mensaje recibido que será procesado.
 */
@FunctionalInterface
public interface MessageListener {
    /**
     * Método que se invoca cuando se recibe un mensaje.
     *
     * @param message El mensaje recibido encapsulado en un objeto MessageModelRequest.
     */
    void onMessageReceived(MessageModelRequest message);
}

