package io.github.jhoanhurtado.domain.models;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class EmailMessageTest {

    @Test
    public void testGetAttachmentInfo() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .attachmentName("document.pdf")
                .attachmentBase64("dGVzdA==")
                .build();
        assertEquals("FileName: document.pdf\nFileBase64: dGVzdA==", emailMessage.getAttachmentInfo());
    }

    @Test
    public void testGetBccRecipients() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .bccRecipients(Arrays.asList("bcc1@example.com", "bcc2@example.com"))
                .build();
        assertEquals(Arrays.asList("bcc1@example.com", "bcc2@example.com"), emailMessage.getBccRecipients());
    }

    @Test
    public void testCcRecipientsNull() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .ccRecipients(null)
                .build();

        assertEquals(null, emailMessage.getCcRecipients());
    }

    @Test
    public void testGetBodyEmpty() {
        String body = "";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .body(body)
                .build();

        assertEquals(body, emailMessage.getBody());
    }

    @Test
    public void testGetBodyNull() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .body(null)
                .build();

        assertEquals(null, emailMessage.getBody());
    }

    @Test
    public void testGetBody() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .body("This is the body of the email")
                .build();
        assertEquals("This is the body of the email", emailMessage.getBody());
    }

    @Test
    public void testGetCcRecipients() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .ccRecipients(Arrays.asList("cc1@example.com", "cc2@example.com"))
                .build();
        assertEquals(Arrays.asList("cc1@example.com", "cc2@example.com"), emailMessage.getCcRecipients());
    }

    @Test
    public void testGetContent() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient1@example.com"))
                .ccRecipients(Collections.emptyList())
                .bccRecipients(Collections.emptyList())
                .subject("Test Subject")
                .body("Test Body")
                .isHtml(false)
                .attachmentName("document.pdf")
                .attachmentBase64("dGVzdA==")
                .build();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Sender: sender@example.com\n");
        stringBuilder.append("Recipients: recipient1@example.com\n");
        stringBuilder.append("CC: \n");
        stringBuilder.append("BCC: \n");
        stringBuilder.append("Subject: Test Subject\n");
        stringBuilder.append("Body: Test Body\n");
        stringBuilder.append("Is HTML: false\n");
        stringBuilder.append("Attachment Name: document.pdf\n");
        stringBuilder.append("Attachment (Base64): dGVzdA==");
        String expectedContent = stringBuilder.toString();
        assertEquals(expectedContent, emailMessage.getContent());
    }

    @Test
    public void testGetDestination() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .build();
        assertEquals("sender@example.com", emailMessage.getDestination());
    }

    @Test
    public void testGetFullEmailInfo() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient1@example.com"))
                .ccRecipients(Arrays.asList("cc1@example.com"))
                .bccRecipients(Arrays.asList("bcc1@example.com"))
                .build();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Remitente: sender@example.com\n");
        stringBuilder.append("Destinatarios: recipient1@example.com\n");
        stringBuilder.append("CC: cc1@example.com\n");
        stringBuilder.append("CCO: bcc1@example.com");
        String expectedInfo = stringBuilder.toString();
        assertEquals(expectedInfo, emailMessage.getFullEmailInfo());
    }

    @Test
    public void testGetRecipients() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .recipients(Arrays.asList("recipient1@example.com", "recipient2@example.com"))
                .build();
        assertEquals(Arrays.asList("recipient1@example.com", "recipient2@example.com"), emailMessage.getRecipients());
    }

    @Test
    public void testGetSubject() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .subject("Test Subject")
                .build();
        assertEquals("Test Subject", emailMessage.getSubject());
    }

    @Test
    public void testIsHtml() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .isHtml(true)
                .build();
        assertTrue(emailMessage.isHtml());
    }
}
