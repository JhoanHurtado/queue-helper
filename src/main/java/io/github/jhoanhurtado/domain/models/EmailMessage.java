package io.github.jhoanhurtado.domain.models;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;

/**
 * Representa un mensaje de correo electrónico que será enviado a una cola en un
 * broker de mensajería como RabbitMQ o Kafka para ser procesado por un servicio
 * de notificaciones.
 */
public class EmailMessage implements MessageModel, Serializable {

    /**
     * Correo electrónico del remitente
     */
    private final String senderEmail;

    /**
     * Lista de destinatarios principales
     */
    private final List<String> recipients;

    /**
     * Lista de destinatarios en copia (CC - Carbon Copy)
     */
    private final List<String> ccRecipients;

    /**
     * Lista de destinatarios en copia oculta (CCO - Blind Carbon Copy)
     */
    private final List<String> bccRecipients;

    /**
     * Asunto del correo electrónico
     */
    private final String subject;

    /**
     * Cuerpo del correo electrónico
     */
    private final String body;

    /**
     * Indica si el contenido es HTML (true) o texto plano (false)
     */
    private final boolean isHtml;

    /**
     * Datos del archivo adjunto codificados en Base64 (opcional)
     */
    private final String attachmentBase64;

    /**
     * Nombre del archivo adjunto (opcional)
     */
    private final String attachmentName;

    @JsonCreator
    public EmailMessage(
            @JsonProperty("sender") String sender,
            @JsonProperty("recipients") List<String> recipients,
            @JsonProperty("ccRecipients") List<String> ccRecipients,
            @JsonProperty("bccRecipients") List<String> bccRecipients,
            @JsonProperty("subject") String subject,
            @JsonProperty("body") String body,
            @JsonProperty("isHtml") boolean isHtml,
            @JsonProperty("attachmentName") String attachmentName,
            @JsonProperty("attachmentBase64") String attachmentBase64) {

        this.senderEmail = sender;
        this.recipients = recipients;
        this.ccRecipients = ccRecipients;
        this.bccRecipients = bccRecipients;
        this.subject = subject;
        this.body = body;
        this.isHtml = isHtml;
        this.attachmentName = attachmentName;
        this.attachmentBase64 = attachmentBase64;
    }

    /**
     * Constructor privado para crear instancias de EmailMessage a través del
     * Builder.
     *
     * @param builder Instancia del Builder con los valores configurados.
     */
    private EmailMessage(Builder builder) {
        this.senderEmail = builder.senderEmail;
        this.recipients = List.copyOf(Optional.ofNullable(builder.recipients).orElse(List.of()));
        this.ccRecipients = List.copyOf(Optional.ofNullable(builder.ccRecipients).orElse(List.of()));
        this.bccRecipients = List.copyOf(Optional.ofNullable(builder.bccRecipients).orElse(List.of()));
        this.subject = builder.subject;
        this.body = builder.body;
        this.isHtml = builder.isHtml;
        this.attachmentBase64 = builder.attachmentBase64;
        this.attachmentName = builder.attachmentName;
    }

    /**
     * Clase Builder para construir instancias de EmailMessage de manera
     * flexible y fluida. Utiliza el patrón de diseño Builder para facilitar la
     * creación de objetos EmailMessage con múltiples atributos opcionales.
     */
    public static class Builder {

        /**
         * Dirección de correo electrónico del remitente. Tipo: String
         */
        private String senderEmail;

        /**
         * Lista de direcciones de correo electrónico de los destinatarios
         * principales. Tipo: List<String>
         */
        private List<String> recipients;

        /**
         * Lista de direcciones de correo electrónico de los destinatarios en
         * copia (CC - Carbon Copy). Tipo: List<String>
         */
        private List<String> ccRecipients = List.of();

        /**
         * Lista de direcciones de correo electrónico de los destinatarios en
         * copia oculta (CCO - Blind Carbon Copy). Tipo: List<String>
         */
        private List<String> bccRecipients = List.of();

        /**
         * Asunto del correo electrónico. Tipo: String
         */
        private String subject;

        /**
         * Contenido del cuerpo del correo electrónico. Puede ser texto plano o
         * HTML, dependiendo del valor de isHtml. Tipo: String
         */
        private String body;

        /**
         * Indica si el contenido del correo está en formato HTML (true) o en
         * texto plano (false). Tipo: boolean
         */
        private boolean isHtml;

        /**
         * Contenido del archivo adjunto codificado en Base64. Tipo: String
         */
        private String attachmentBase64;

        /**
         * Nombre del archivo adjunto, incluyendo su extensión (ejemplo:
         * "documento.pdf"). Tipo: String
         */
        private String attachmentName;

        /**
         * Establece la dirección de correo del remitente.
         *
         * @param senderEmail Dirección de correo electrónico del remitente.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder senderEmail(String senderEmail) {
            this.senderEmail = senderEmail;
            return this;
        }

        /**
         * Establece la lista de destinatarios principales.
         *
         * @param recipients Lista de direcciones de correo electrónico de los
         * destinatarios principales.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder recipients(List<String> recipients) {
            this.recipients = recipients;
            return this;
        }

        /**
         * Establece la lista de destinatarios en copia (CC).
         *
         * @param ccRecipients Lista de direcciones de correo en copia.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder ccRecipients(List<String> ccRecipients) {
            this.ccRecipients = ccRecipients;
            return this;
        }

        /**
         * Establece la lista de destinatarios en copia oculta (CCO).
         *
         * @param bccRecipients Lista de direcciones de correo en copia oculta.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder bccRecipients(List<String> bccRecipients) {
            this.bccRecipients = bccRecipients;
            return this;
        }

        /**
         * Establece el asunto del correo electrónico.
         *
         * @param subject Texto que representa el asunto del correo.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Establece el contenido del cuerpo del correo electrónico.
         *
         * @param body Texto del mensaje que se enviará en el correo.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder body(String body) {
            this.body = body;
            return this;
        }

        /**
         * Define si el contenido del correo está en formato HTML o en texto
         * plano.
         *
         * @param isHtml true si el correo debe interpretarse como HTML, false
         * si es texto plano.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }

        /**
         * Establece el contenido del archivo adjunto en formato Base64.
         *
         * @param attachmentBase64 Cadena en Base64 que representa el contenido
         * del archivo adjunto.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder attachmentBase64(String attachmentBase64) {
            this.attachmentBase64 = attachmentBase64;
            return this;
        }

        /**
         * Establece el nombre del archivo adjunto.
         *
         * @param attachmentName Nombre del archivo adjunto con su extensión
         * (ejemplo: "documento.pdf").
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder attachmentName(String attachmentName) {
            this.attachmentName = attachmentName;
            return this;
        }

        /**
         * Construye y devuelve una instancia de EmailMessage con los valores
         * configurados.
         *
         * @return Nueva instancia de EmailMessage con los valores asignados en
         * el Builder.
         */
        public EmailMessage build() {
            if (senderEmail == null || senderEmail.isBlank()) {
                throw new IllegalArgumentException("El remitente no puede estar vacío.");
            }
            if (recipients == null || recipients.isEmpty()) {
                throw new IllegalArgumentException("Debe haber al menos un destinatario.");
            }
            if (subject == null || subject.isBlank()) {
                throw new IllegalArgumentException("El asunto no puede estar vacío.");
            }
            if (body == null || body.isBlank()) {
                throw new IllegalArgumentException("El cuerpo del mensaje no puede estar vacío.");
            }
            return new EmailMessage(this);

        }
    }

    /**
     * Obtiene el correo electrónico del remitente.
     */
    @Override
    public String getDestination() {
        return senderEmail;
    }

    /**
     * Obtiene el contenido del mensaje, incluyendo el asunto y el cuerpo del
     * correo.
     */
    @Override
    public String getContent() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Sender: " + senderEmail)
                .add("Recipients: " + String.join(", ", Optional.ofNullable(recipients).orElse(List.of())))
                .add("CC: " + String.join(", ", Optional.ofNullable(ccRecipients).orElse(List.of())))
                .add("BCC: " + String.join(", ", Optional.ofNullable(bccRecipients).orElse(List.of())))
                .add("Subject: " + subject)
                .add("Body: " + body)
                .add("Is HTML: " + isHtml)
                .add("Attachment Name: " + attachmentName)
                .add("Attachment (Base64): " + attachmentBase64);
        return joiner.toString();
    }

    /**
     * Obtiene una descripción detallada del correo electrónico, incluyendo: -
     * Remitente - Destinatarios principales - Copias (CC) - Copias ocultas
     * (CCO)
     *
     * @return Cadena con la información estructurada del correo.
     */
    public String getFullEmailInfo() {
        StringJoiner emailInfo = new StringJoiner("\n");

        emailInfo.add("Remitente: " + senderEmail);

        if (recipients != null && !recipients.isEmpty()) {
            emailInfo.add("Destinatarios: " + String.join(", ", recipients));
        }

        if (ccRecipients != null && !ccRecipients.isEmpty()) {
            emailInfo.add("CC: " + String.join(", ", ccRecipients));
        }

        if (bccRecipients != null && !bccRecipients.isEmpty()) {
            emailInfo.add("CCO: " + String.join(", ", bccRecipients));
        }

        return emailInfo.toString();
    }

    /**
     * Getters para los atributos (necesarios para serialización).
     *
     * @return
     */
    // Removed redundant method getSenderEmail
    public List<String> getRecipients() {
        return recipients;
    }

    public List<String> getCcRecipients() {
        return ccRecipients;
    }

    /**
     * Recupera la lista de destinatarios en copia oculta (CCO) del mensaje de
     * correo electrónico.
     *
     * @return una lista de direcciones de correo electrónico que son
     * destinatarios en copia oculta.
     */
    public List<String> getBccRecipients() {
        return bccRecipients;
    }

    /**
     * Recupera el asunto del mensaje de correo electrónico.
     *
     * @return el asunto del mensaje de correo electrónico como una cadena.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Recupera el cuerpo del mensaje de correo electrónico.
     *
     * @return el cuerpo del mensaje de correo electrónico como una cadena.
     */
    public String getBody() {
        return body;
    }

    /**
     * Verifica si el mensaje de correo electrónico está en formato HTML.
     *
     * @return true si el mensaje de correo está en formato HTML, false en caso
     * contrario.
     */
    public boolean isHtml() {
        return isHtml;
    }

    /**
     * Recupera información sobre el archivo adjunto del correo electrónico.
     *
     * @return Una cadena que contiene el nombre del archivo adjunto y su
     * contenido codificado en base64. Si no hay archivos adjuntos, devuelve "No
     * hay archivos adjuntos."
     */
    public String getAttachmentInfo() {
        if (attachmentName == null || attachmentBase64 == null) {
            return "No hay archivos adjuntos.";
        }
        return "FileName: " + attachmentName + "\nFileBase64: " + attachmentBase64;
    }

    @Override
    public String toString() {
        return "EmailMessage{"
                + "senderEmail='" + senderEmail + '\''
                + ", recipients=" + recipients
                + ", ccRecipients=" + ccRecipients
                + ", bccRecipients=" + bccRecipients
                + ", subject='" + subject + '\''
                + ", isHtml=" + isHtml
                + ", attachmentName='" + attachmentName + '\''
                + '}';
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"sender\": \"").append(senderEmail).append("\", ");
        json.append("\"recipients\": ").append(formatArray(recipients)).append(", ");
        json.append("\"ccRecipients\": ").append(formatArray(ccRecipients)).append(", ");
        json.append("\"bccRecipients\": ").append(formatArray(bccRecipients)).append(", ");
        json.append("\"subject\": \"").append(subject).append("\", ");
        json.append("\"body\": \"").append(body).append("\", ");
        json.append("\"isHtml\": ").append(isHtml).append(", ");
        json.append("\"attachmentName\": \"").append(attachmentName).append("\", ");
        json.append("\"attachmentBase64\": \"").append(attachmentBase64).append("\"");
        json.append("}");
        return json.toString();
    }

    private String formatArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return "[" + list.stream()
                .map(item -> "\"" + item + "\"")
                .collect(Collectors.joining(", ")) + "]";
    }
}
