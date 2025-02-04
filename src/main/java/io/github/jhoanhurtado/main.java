package io.github.jhoanhurtado;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhoanhurtado.application.facades.MessagingFacade;
import io.github.jhoanhurtado.application.facades.QueueHelper;
import io.github.jhoanhurtado.domain.models.EmailMessage;
import io.github.jhoanhurtado.domain.models.MessageModelRequest;
import io.github.jhoanhurtado.domain.strategies.RabbitMQConsumer;
import io.github.jhoanhurtado.domain.strategies.RabbitMQStrategy;
import io.github.jhoanhurtado.observer.MessageObserver;

public class Main {

    public static void main(String[] args) throws Exception {

        String rabbitmqMessage = "queue-email-message-sending";
        EmailMessage email = new EmailMessage.Builder()
                .senderEmail("example@example.com")
                .recipients(List.of("recipient1@example.com", "recipient2@example.com"))
                .ccRecipients(List.of("cc1@example.com"))
                .bccRecipients(List.of("bcc1@example.com"))
                .subject("Subject of the email")
                .body("Body of the email")
                .isHtml(true)
                .attachmentBase64("base64EncodedString")
                .attachmentName("attachment.pdf")
                .build();

        // Crear la instancia de la fachada QueueHelper
        QueueHelper queueHelper = new QueueHelper();

        // Conectar a RabbitMQ (puedes conectar a diferentes brokers con nombres únicos)
        queueHelper.withRabbitMQ(rabbitmqMessage, "18.117.106.167", "application", "GlobalStore");

        // Usar la fachada con RabbitMQ
        MessagingFacade messagingFacade = new MessagingFacade(
                new RabbitMQStrategy(queueHelper.getConnection(rabbitmqMessage)));

        messagingFacade.send(rabbitmqMessage, email, 50, 1);

        // Cerrar la conexión después de usarla
        //queueHelper.disconnect(RABBITMQ_MESSAGE);

        // 🔹 Crear el observador
        MessageObserver observer = new MessageObserver();

        // 🔹 Suscribir el callback (lambda)
        observer.subscribe((MessageModelRequest message) -> {

            try {
                var msg = message.getContent();
                ObjectMapper objectMapper = new ObjectMapper();
                EmailMessage emailMessage = objectMapper.readValue(msg, EmailMessage.class);
                System.out.println("✉️ Email: " + emailMessage);
            } catch (JsonProcessingException e) {
                System.err.println("Error al procesar el mensaje: " + e.getMessage());
            }

        });

        // 🔹 Iniciar el consumidor de RabbitMQ
        RabbitMQConsumer consumer = new RabbitMQConsumer(observer, queueHelper.getConnection(rabbitmqMessage));
        consumer.startListening();
    }
}
