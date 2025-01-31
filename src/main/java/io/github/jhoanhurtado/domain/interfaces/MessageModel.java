package io.github.jhoanhurtado.domain.interfaces;

/**
 * Interface que define el modelo de un mensaje que será enviado a una cola de mensajería.
 * Proporciona métodos para obtener la información esencial del mensaje.
 */
public interface MessageModel {

    /**
     * Obtiene la dirección de destino del mensaje.
     * 
     * @return La dirección de destino del mensaje.
     */
    String getDestination();

    /**
     * Obtiene el contenido completo del mensaje, incluyendo el asunto y el cuerpo del correo.
     * 
     * @return El contenido del mensaje.
     */
    String getContent();
}