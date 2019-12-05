package edu.lehigh.cse216.kta221.backend;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

import static java.lang.String.*;

public class EmailSender {

    private final static Email from = new Email("alex.r.koby@gmail.com");
    private final static String subject = "Welcome to Clowns Who Code";
    private final static String sgAPIKey = "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";

    /**
     * @return returns true if email sends properly, false if it doesn't
     */
    public static boolean sendEmail(String emailAddr, String pass) {
        Email recipient = new Email(emailAddr);
        Content content = new Content("text/plain", format("Welcome to clowns who code. Your password is: %s", pass));
        Mail email = new Mail(from, subject, recipient, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(email.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println(String.format("Issue sending email: %s", ex.getMessage()));
            return false;
        }
        return true;
    }
}
