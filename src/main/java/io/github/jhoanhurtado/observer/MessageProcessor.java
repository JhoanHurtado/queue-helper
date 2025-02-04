package io.github.jhoanhurtado.observer;

import java.util.ArrayList;
import java.util.List;

import io.github.jhoanhurtado.domain.models.MessageModelRequest;

/**
 * La clase MessageProcessor se encarga de gestionar la suscripción y notificación de mensajes
 * a los oyentes registrados.
 * 
 * <p>Esta clase permite que los objetos que implementan la interfaz MessageListener se suscriban
 * para recibir notificaciones cuando se recibe un nuevo mensaje.</p>
 * 
 * <p>Métodos:</p>
 * <ul>
 *   <li>{@link #subscribe(MessageListener)}: Permite que un oyente se suscriba para recibir notificaciones de mensajes.</li>
 *   <li>{@link #notify(MessageModelRequest)}: Notifica a todos los oyentes suscritos cuando se recibe un nuevo mensaje.</li>
 * </ul>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * {@code
 * MessageProcessor processor = new MessageProcessor();
 * processor.subscribe(new MessageListener() {
 *     @Override
 *     public void onMessageReceived(MessageModelRequest message) {
 *         // Manejar el mensaje recibido
 *     }
 * });
 * 
 * MessageModelRequest message = new MessageModelRequest();
 * processor.notify(message);
 * }
 * </pre>
 */
public class MessageProcessor {
    private final List<MessageListener> listeners = new ArrayList<>();

    /**
     * Subscribes a new listener to the message processor.
     * 
     * @param listener the MessageListener to be added to the list of listeners
     */
    /**
     * Suscribe un nuevo oyente de mensajes a la lista de oyentes.
     *
     * @param listener El oyente de mensajes que se va a suscribir.
     */
    public void subscribe(MessageListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered listeners with the given message.
     *
     * @param message the message to be sent to the listeners
     */
    /**
     * Notifica a todos los oyentes registrados con el mensaje recibido.
     *
     * @param message El mensaje que se va a notificar a los oyentes.
     */
    public void notify(MessageModelRequest message) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(message);
        }
    }
}