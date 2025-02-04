package io.github.jhoanhurtado.domain.models;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EmailMessageTest {

    @Test
    public void testRecipients() {
        List<String> recipients = Arrays.asList("recipient1@example.com", "recipient2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .subject("Test Subject")
                .body("Test Body")
                .build();

        assertEquals(recipients, emailMessage.getRecipients());
    }

    @Test
    public void testCcRecipients() {
        List<String> ccRecipients = Arrays.asList("cc1@example.com", "cc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .ccRecipients(ccRecipients)
                .subject("Test Subject")
                .body("Test Body")
                .build();

        assertEquals(ccRecipients, emailMessage.getCcRecipients());
    }

    @Test
    public void testBccRecipients() {
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .bccRecipients(bccRecipients)
                .subject("Test Subject")
                .body("Test Body")
                .build();

        assertEquals(bccRecipients, emailMessage.getBccRecipients());
    }

    @Test
    public void testSubject() {
        String subject = "Test Subject";
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .bccRecipients(bccRecipients)
                .subject(subject)
                .body("Test Body")
                .build();

        assertEquals(subject, emailMessage.getSubject());
    }

    @Test
    public void testIsHtml() {
        boolean isHtml = true;
        String subject = "Test Subject";
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .bccRecipients(bccRecipients)
                .subject(subject)
                .body("Test Body")
                .isHtml(isHtml)
                .build();
        assertEquals(isHtml, emailMessage.isHtml());
    }

    @Test
    public void testIsNotHtml() {
        boolean isHtml = false;

        String subject = "Test Subject";
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .bccRecipients(bccRecipients)
                .subject(subject)
                .body("Test Body")
                .build();

        assertEquals(isHtml, emailMessage.isHtml());
    }

    @Test
    public void testAttachmentName() {
        String attachmentName = "document.pdf";

        String subject = "Test Subject";
        List<String> bccRecipients = Arrays.asList("bcc1@example.com", "bcc2@example.com");

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .bccRecipients(bccRecipients)
                .subject(subject)
                .attachmentName(attachmentName)
                .attachmentBase64("dGVzdCBhdHRhY2htZW50")
                .body("Test Body")
                .build();

        String[] attachmentInfo = emailMessage.getAttachmentInfo().split("\n");
        assertEquals("FileName: " + attachmentName, attachmentInfo[0]);
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
                .subject("subject")
                .body("body")
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
                .subject("Test Subject")
                .body("body")
                .build();

        String expectedInfo = String.join("\n",
                "Remitente: sender@example.com",
                "Destinatarios: recipient1@example.com, recipient2@example.com");

        assertEquals(expectedInfo, emailMessage.getFullEmailInfo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRecipientsEmpty() {
        List<String> recipients = Arrays.asList();

        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(recipients)
                .build();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRecipientsNull() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("body")
                .recipients(null)
                .build();
    }

    @Test
    public void testCcRecipientsEmpty() {
        List<String> ccRecipients = Arrays.asList();

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .ccRecipients(ccRecipients)
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("body")
                .build();

        assertEquals(ccRecipients, emailMessage.getCcRecipients());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCcRecipientsNull() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .ccRecipients(null)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBodyEmpty() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBodyNull() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body(null)
                .build();
    }

    @Test
    public void testGetBody() {
        String body = "This is a test email body.";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body(body)
                .build();

        assertEquals(body, emailMessage.getBody());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithoutSenderEmail() {
        new EmailMessage.Builder()
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("Test Body")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithEmptySenderEmail() {
        new EmailMessage.Builder()
                .senderEmail("")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("Test Body")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithoutRecipients() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithEmptyRecipients() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList())
                .subject("Test Subject")
                .body("Test Body")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithoutSubject() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .body("Test Body")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithEmptySubject() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("")
                .body("Test Body")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithoutBody() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithEmptyBody() {
        new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("")
                .build();
    }

    @Test
    public void testAttachmentBase64() {
        String attachmentBase64 = "dGVzdCBhdHRhY2htZW50"; // "test attachment" in Base64

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("Test Body")
                .attachmentName("test.txt")
                .attachmentBase64(attachmentBase64)
                .build();

        String[] attachmentInfo = emailMessage.getAttachmentInfo().split("\n");
        assertEquals("FileName: test.txt", attachmentInfo[0]);
        assertEquals("FileBase64: " + attachmentBase64, attachmentInfo[1]);
    }

    @Test
    public void testAttachmentBase64Null() {
        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("Test Body")
                .attachmentBase64(null)
                .build();

        assertEquals("No hay archivos adjuntos.", emailMessage.getAttachmentInfo());
    }

    @Test
    public void testAttachmentBase64Empty() {
        String attachmentBase64 = "";

        EmailMessage emailMessage = new EmailMessage.Builder()
                .senderEmail("sender@example.com")
                .recipients(Arrays.asList("recipient@example.com"))
                .subject("Test Subject")
                .body("Test Body")
                .attachmentBase64(attachmentBase64)
                .build();

        assertEquals("No hay archivos adjuntos.", emailMessage.getAttachmentInfo());
    }
}
