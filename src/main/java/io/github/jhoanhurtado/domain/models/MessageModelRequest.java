package io.github.jhoanhurtado.domain.models;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * Representa un modelo de solicitud para un mensaje.
 * Esta clase incluye el contenido del mensaje y la información del remitente.
 * También proporciona métodos para convertir desde JSON y generar una representación en cadena.
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * {@code
 * MessageModelRequest request = new MessageModelRequest("¡Hola, Mundo!", "John Doe");
 * String json = new Gson().toJson(request);
 * MessageModelRequest fromJson = MessageModelRequest.fromJson(json);
 * }
 * </pre>
 * 
 * <p>Nota: Esta clase utiliza Gson para la serialización y deserialización de JSON.</p>
 * 
 * @see com.google.gson.Gson
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageModelRequest {
    private final String content;
    private final String sender;

    /**
     * Construye un nuevo MessageModelRequest con el contenido y remitente especificados.
     *
     * @param content el contenido del mensaje
     * @param sender el remitente del mensaje
     */
    public MessageModelRequest(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    /**
     * Recupera el contenido del mensaje.
     *
     * @return el contenido del mensaje como una cadena.
     */
    public String getContent() {
        return content;
    }

    /**
     * Recupera el remitente del mensaje.
     *
     * @return el remitente del mensaje como una cadena.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Convierte una cadena JSON en un objeto MessageModelRequest.
     *
     * @param json la cadena JSON a convertir
     * @return un objeto MessageModelRequest analizado desde la cadena JSON
     */
    public static MessageModelRequest fromJson(String json) {
        return new Gson().fromJson(json, MessageModelRequest.class);
    }

    /**
     * Devuelve una representación en cadena del objeto MessageModelRequest.
     * La cadena incluye los campos de contenido y remitente.
     *
     * @return una representación en cadena del objeto MessageModelRequest
     */
    @Override
    public String toString() {
        return "MessageModel{" +
                "content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}