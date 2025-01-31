package models;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.github.jhoanhurtado.domain.models.EmailMessage;

public class EmailMessageTest {

    @Test
    public void testRecipients() {
        List<String> recipients = Arrays.asList("recipient1@example.com", "recipient2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .build();

        assertEquals(recipients, emailMessage.getRecipients());
    }

    @Test
    public void testCcRecipients() {
        List<String> ccRecipients = Arrays.asList("cc1@example.com", "cc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .ccRecipients(ccRecipients)
                .build();

        assertEquals(ccRecipients, emailMessage.getCcRecipients());
    }

    @Test
    public void testBccRecipients() {
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .bccRecipients(bccRecipients)
                .build();

        assertEquals(bccRecipients, emailMessage.getBccRecipients());
    }

    @Test
    public void testSubject() {
        String subject = "Test Subject";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .subject(subject)
                .build();

        assertEquals(subject, emailMessage.getSubject());
    }

    @Test
    public void testIsHtml() {
        boolean isHtml = true;

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .isHtml(isHtml)
                .build();

        assertEquals(isHtml, emailMessage.isHtml());
    }

    @Test
    public void testIsNotHtml() {
        boolean isHtml = false;

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .isHtml(isHtml)
                .build();

        assertEquals(isHtml, emailMessage.isHtml());
    }

    @Test
    public void testAttachmentBase64() {
        String attachmentBase64 = "dGVzdCBhdHRhY2htZW50"; // "test attachment" in Base64

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .attachmentBase64(attachmentBase64)
                .build();

        assertEquals(attachmentBase64,
                emailMessage.getAttachmentInfo().split("\n")[1].split(": ")[1]);
    }

    @Test
    public void testAttachmentName() {
        String attachmentName = "document.pdf";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .attachmentName(attachmentName)
                .build();

        assertEquals(attachmentName,
                emailMessage.getAttachmentInfo().split("\n")[0].split(": ")[1]);
    }


    @Test
    public void testGetContent() {
        List<String> recipients = Arrays.asList("recipient1@example.com",
                "recipient2@example.com");
        List<String> ccRecipients = Arrays.asList("cc1@example.com", "cc2@example.com");
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");
        String subject = "Test Subject";
        String body = "This is a test email body.";
        boolean isHtml = true;
        String attachmentBase64 = "dGVzdCBhdHRhY2htZW50"; // "test attachment" in Base64
        String attachmentName = "document.pdf";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .ccRecipients(ccRecipients)
                .bccRecipients(bccRecipients)
                .subject(subject)
                .body(body)
                .isHtml(isHtml)
                .attachmentBase64(attachmentBase64)
                .attachmentName(attachmentName)
                .build();

        String expectedContent = String.join("\n",
                "Sender: sender@example.com",
                "Recipients: recipient1@example.com, recipient2@example.com",
                "CC: cc1@example.com, cc2@example.com",
                "BCC: bcc1@example.com, bcc2@example.com",
                "Subject: Test Subject",
                "Body: This is a test email body.",
                "Is HTML: true",
                "Attachment Name: document.pdf",
                "Attachment (Base64): dGVzdCBhdHRhY2htZW50");

        assertEquals(expectedContent, emailMessage.getContent());
    }


    @Test
    public void testGetFullEmailInfo() {
        List<String> recipients = Arrays.asList("recipient1@example.com",
                "recipient2@example.com");
        List<String> ccRecipients = Arrays.asList("cc1@example.com", "cc2@example.com");
        List<String> bccRecipients = Arrays.asList("bcc1@example.com",
                "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .ccRecipients(ccRecipients)
                .bccRecipients(bccRecipients)
                .build();

        String expectedInfo = String.join("\n",
                "Remitente: sender@example.com",
                "Destinatarios: recipient1@example.com, recipient2@example.com",
                "CC: cc1@example.com, cc2@example.com",
                "CCO: bcc1@example.com, bcc2@example.com");

        assertEquals(expectedInfo, emailMessage.getFullEmailInfo());
    }


    @Test
    public void testGetFullEmailInfoWithoutCcAndBcc() {
        List<String> recipients = Arrays.asList("recipient1@example.com",
                "recipient2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .build();

        String expectedInfo = String.join("\n",
                "Remitente: sender@example.com",
                "Destinatarios: recipient1@example.com, recipient2@example.com");

        assertEquals(expectedInfo, emailMessage.getFullEmailInfo());
    }

    @Test
    public void testGetFullEmailInfoWithoutRecipients() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .build();

        String expectedInfo = "Remitente: sender@example.com";

        assertEquals(expectedInfo, emailMessage.getFullEmailInfo());
    }

    @Test
    public void testRecipientsEmpty() {
        List<String> recipients = Arrays.asList();

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .build();

        assertEquals(recipients, emailMessage.getRecipients());
    }

    @Test
    public void testRecipientsNull() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(null)
                .build();

        assertEquals(null, emailMessage.getRecipients());
    }

    @Test
    public void testCcRecipientsEmpty() {
        List<String> ccRecipients = Arrays.asList();

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .ccRecipients(ccRecipients)
                .build();

        assertEquals(ccRecipients, emailMessage.getCcRecipients());
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
        String body = "This is a test email body.";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .body(body)
                .build();

        assertEquals(body, emailMessage.getBody());
    }

}