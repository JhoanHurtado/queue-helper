package io.github.jhoanhurtado.domain.models;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

import io.github.jhoanhurtado.domain.interfaces.MessageModel;
/**
 * Representa un mensaje de correo electrónico que será enviado a una cola en un
 * broker de mensajería
 * como RabbitMQ o Kafka para ser procesado por un servicio de notificaciones.
 */
public class EmailMessage implements MessageModel, Serializable {

    /** Correo electrónico del remitente */
    private final String senderEmail;

    /** Lista de destinatarios principales */
    private final List<String> recipients;

    /** Lista de destinatarios en copia (CC - Carbon Copy) */
    private final List<String> ccRecipients;

    /** Lista de destinatarios en copia oculta (CCO - Blind Carbon Copy) */
    private final List<String> bccRecipients;

    /** Asunto del correo electrónico */
    private final String subject;

    /** Cuerpo del correo electrónico */
    private final String body;

    /** Indica si el contenido es HTML (true) o texto plano (false) */
    private final boolean isHtml;

    /** Datos del archivo adjunto codificados en Base64 (opcional) */
    private final String attachmentBase64;

    /** Nombre del archivo adjunto (opcional) */
    private final String attachmentName;

    private EmailMessage(Builder builder) {
        this.senderEmail = builder.senderEmail;
        this.recipients = builder.recipients;
        this.ccRecipients = builder.ccRecipients;
        this.bccRecipients = builder.bccRecipients;
        this.subject = builder.subject;
        this.body = builder.body;
        this.isHtml = builder.isHtml;
        this.attachmentBase64 = builder.attachmentBase64;
        this.attachmentName = builder.attachmentName;
    }

    /**
     * Clase Builder para construir instancias de EmailMessage de manera flexible y
     * fluida.
     * Utiliza el patrón de diseño Builder para facilitar la creación de objetos
     * EmailMessage
     * con múltiples atributos opcionales.
     *
     * Parámetros:
     * 
     * @param senderEmail      (String):
     *                         Dirección de correo electrónico del remitente.
     * 
     * @param recipients       (List<String>):
     *                         Lista de direcciones de correo de los destinatarios
     *                         principales.
     * 
     * @param ccRecipients     (List<String>):
     *                         Lista de direcciones de correo de los destinatarios
     *                         en copia (CC).
     * 
     * @param bccRecipients    (List<String>):
     *                         Lista de direcciones de correo de los destinatarios
     *                         en copia oculta (CCO).
     * 
     * @param subject          (String):
     *                         Asunto del correo electrónico.
     * 
     * @param body             (String):
     *                         Contenido del cuerpo del correo, puede ser texto
     *                         plano o HTML.
     * 
     * @param isHtml           (boolean):
     *                         Indica si el contenido del correo está en formato
     *                         HTML (true) o en texto
     *                         plano (false).
     * 
     * @param attachmentBase64 (String):
     *                         Contenido del archivo adjunto codificado en Base64.
     * 
     * @param attachmentName   (String):
     *                         Nombre del archivo adjunto, incluyendo su extensión
     *                         (ejemplo:
     *                         "documento.pdf").
     */
    public static class Builder {

        /**
         * Dirección de correo electrónico del remitente.
         * Tipo: String
         */
        private String senderEmail;

        /**
         * Lista de direcciones de correo electrónico de los destinatarios principales.
         * Tipo: List<String>
         */
        private List<String> recipients;

        /**
         * Lista de direcciones de correo electrónico de los destinatarios en copia (CC
         * - Carbon Copy).
         * Tipo: List<String>
         */
        private List<String> ccRecipients;

        /**
         * Lista de direcciones de correo electrónico de los destinatarios en copia
         * oculta (CCO - Blind Carbon Copy).
         * Tipo: List<String>
         */
        private List<String> bccRecipients;

        /**
         * Asunto del correo electrónico.
         * Tipo: String
         */
        private String subject;

        /**
         * Contenido del cuerpo del correo electrónico.
         * Puede ser texto plano o HTML, dependiendo del valor de isHtml.
         * Tipo: String
         */
        private String body;

        /**
         * Indica si el contenido del correo está en formato HTML (true) o en texto
         * plano (false).
         * Tipo: boolean
         */
        private boolean isHtml;

        /**
         * Contenido del archivo adjunto codificado en Base64.
         * Tipo: String
         */
        private String attachmentBase64;

        /**
         * Nombre del archivo adjunto, incluyendo su extensión (ejemplo:
         * "documento.pdf").
         * Tipo: String
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
         *                   destinatarios principales.
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
         * Define si el contenido del correo está en formato HTML o en texto plano.
         * 
         * @param isHtml true si el correo debe interpretarse como HTML, false si es
         *               texto plano.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }

        /**
         * Establece el contenido del archivo adjunto en formato Base64.
         * 
         * @param attachmentBase64 Cadena en Base64 que representa el contenido del
         *                         archivo adjunto.
         * @return Instancia del Builder para encadenamiento de métodos.
         */
        public Builder attachmentBase64(String attachmentBase64) {
            this.attachmentBase64 = attachmentBase64;
            return this;
        }

        /**
         * Establece el nombre del archivo adjunto.
         * 
         * @param attachmentName Nombre del archivo adjunto con su extensión (ejemplo:
         *                       "documento.pdf").
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
         * @return Nueva instancia de EmailMessage con los valores asignados en el
         *         Builder.
         */
        public EmailMessage build() {
            return new EmailMessage(this);
        }
    }

    /** Obtiene el correo electrónico del remitente. */
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
                .add("Recipients: " + String.join(", ", recipients))
                .add("CC: " + String.join(", ", ccRecipients))
                .add("BCC: " + String.join(", ", bccRecipients))
                .add("Subject: " + subject)
                .add("Body: " + body)
                .add("Is HTML: " + isHtml)
                .add("Attachment Name: " + attachmentName)
                .add("Attachment (Base64): " + attachmentBase64);
        return joiner.toString();
    }

    /**
     * Obtiene una descripción detallada del correo electrónico, incluyendo:
     * - Remitente
     * - Destinatarios principales
     * - Copias (CC)
     * - Copias ocultas (CCO)
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

    /** Getters para los atributos (necesarios para serialización). */
    // Removed redundant method getSenderEmail
    public List<String> getRecipients() {
        return recipients;
    }

    public List<String> getCcRecipients() {
        return ccRecipients;
    }

    public List<String> getBccRecipients() {
        return bccRecipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public String getAttachmentInfo() {
        return "FileName: " + attachmentName + "\nFileBase64: " + attachmentBase64;
    }
}
