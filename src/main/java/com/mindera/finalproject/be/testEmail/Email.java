package com.mindera.finalproject.be.testEmail;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Email {

    @Inject
    Mailer mailer;

    public void sendEmail(String to) {
        System.out.println("cheguei aqui");
        mailer.send(Mail.withText(to, "test", "test"));
    }

  /*  public void sendWelcomeEmail(String to) {
        System.out.println("cheguei aqui");
        String htmlBody =

                "<h1>Welcome to Our Courses!</h1>" +
                "<p>Dear [Name],</p>" + // You can replace [Name] with the recipient's name
                "<p>Welcome to our community of learners! We are thrilled to have you join us on this journey of learning and growth.</p>" +
                "<p>With our wide range of courses, taught by industry experts, you'll have the opportunity to expand your knowledge and skills in areas that matter to you.</p>" +
                "<p>We can't wait to see all that you'll achieve with us!</p>" +
                "<p>Click the button below to get started:</p>" +
                "<p><a href=\"[Link]\" class=\"button\">Start Learning</a></p>" + // Replace [Link] with the appropriate URL
                "<p>If you have any questions or need assistance, feel free to reach out to us at [Contact Email].</p>" + // Replace [Contact Email] with your contact email
                "<p>Best regards,<br>The [Your Company] Team</p>"; // Replace [Your Company] with your company name


        mailer.send(Mail.withHtml(to, "Welcome to Course Applications", htmlBody));
    }

    public void sendEmailWithCertificate(String to, String body) {
        mailer.send(Mail.withHtml(to, "Congratulations you have completed the course!", "TODO BODY OF EMAIL").addAttachment("certificate.pdf", "certificate.pdf".getBytes(), "application/pdf"));
    }
*/

}
