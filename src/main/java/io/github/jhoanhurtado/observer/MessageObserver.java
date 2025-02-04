package io.github.jhoanhurtado.observer;

import java.util.ArrayList;
import java.util.List;

import io.github.jhoanhurtado.domain.models.MessageModelRequest;

/**
 * La clase MessageObserver permite suscribir oyentes y notificarles cuando se recibe un mensaje.
 * Los oyentes deben implementar la interfaz MessageListener.
 * 
 * <p>Esta clase proporciona los siguientes métodos:</p>
 * <ul>
 *   <li>{@link #subscribe(MessageListener)} - Suscribe un nuevo oyente al observador de mensajes.</li>
 *   <li>{@link #notify(MessageModelRequest)} - Notifica a todos los oyentes registrados con el mensaje dado.</li>
 * </ul>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * MessageObserver observer = new MessageObserver();
 * observer.subscribe(new MessageListener() {
 *     @Override
 *     public void onMessageReceived(MessageModelRequest message) {
 *         // Manejar el mensaje recibido
 *     }
 * });
 * observer.notify(new MessageModelRequest("Mensaje de prueba"));
 * }</pre>
 * 
 * @see MessageListener
 * @see MessageModelRequest
 */
public class MessageObserver {
    private final List<MessageListener> listeners = new ArrayList<>();

    /**
     * Suscribe un nuevo oyente al observador de mensajes.
     *
     * @param listener el MessageListener que se añadirá a la lista de oyentes
     */
    public void subscribe(MessageListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifica a todos los oyentes registrados con el mensaje dado.
     *
     * @param message el mensaje que se enviará a los oyentes
     */
    public void notify(MessageModelRequest message) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }
}