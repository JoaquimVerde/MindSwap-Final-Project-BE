package com.mindera.finalproject.be.email;

import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Registration;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.MailerName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Email {

    @Inject
    @MailerName("outlook")
    Mailer mailer;

    public void sendAccountVerificationEmail(Person person) {
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Verify Your Account</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f7f7f7;\n" +
                "            color: #333;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 40px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #007bff;\n" +
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            padding: 10px 20px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Verify Your Account</h1>\n" +
                "        <p>Hello " + person.getFirstName() + ",</p>\n" +
                "        <p>Welcome to Courses Platform! To activate your account, please click the button below to verify your email address:</p>\n" +
                "        <p><a href=\"http://localhost:8080/api/v1/authVerify/" + person.getSK() + "\" class=\"btn\">Verify Email</a></p>\n" + // TODO CHANGE URL TO PRODUCTION URL
                "        <p>If you didn't request this verification, you can safely ignore this email.</p>\n" +
                "        <p>Best regards,</p>\n" +
                "        <p>The Courses Platform Team</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        try {
            if (mailer == null) {
                throw new IllegalStateException("Mailer instance is not injected.");
            }
            System.out.println("Sending email to: " + person.getEmail());
            mailer.send(Mail.withHtml(person.getEmail(), "test", "test"));
            System.out.println("Email sent successfully to: " + person.getEmail());
        } catch (Exception e) {
            System.err.println("Error sending email to " + person.getEmail() + ": " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(Person person) {
        String htmlBody =
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Welcome to Our Course Platform!</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f7f7f7;\n" +
                        "            color: #333;\n" +
                        "            padding: 20px;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background-color: #fff;\n" +
                        "            padding: 40px;\n" +
                        "            border-radius: 8px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "            text-align: center; /* Center the text */\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #007bff;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            font-size: 16px;\n" +
                        "            line-height: 1.6;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .btn {\n" +
                        "            display: inline-block;\n" +
                        "            background-color: #007bff;\n" +
                        "            color: #fff;\n" +
                        "            padding: 10px 20px;\n" +
                        "            text-decoration: none;\n" +
                        "            border-radius: 5px;\n" +
                        "        }\n" +
                        "        .btn:hover {\n" +
                        "            background-color: #0056b3;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <h1>Welcome to Our Course Platform!</h1>\n" +
                        "        <p>Hello " + person.getFirstName() + ",</p>\n" +
                        "        <p>We're thrilled to have you join our community! You're now part of an exciting journey of learning and growth.</p>\n" +
                        "        <p>Here are a few things you can do:</p>\n" +
                        "        <ul style=\"text-align: left; margin-left: 40px;\"> <!-- Align list items to the left with some margin -->\n" +
                        "            <li>Explore our courses and find the perfect one for you.</li>\n" +
                        "            <li>Start learning and unlock new opportunities!</li>\n" +
                        "        </ul>\n" +
                        "        <p>Ready to get started? Click the button below to log in to your account:</p>\n" +
                        "        <p><a href=\"http://localhost:8080/login \" class=\"btn\">Log In</a></p>\n" +  // TODO CHANGE LOGIN LINK
                        "        <p>If you have any questions or need assistance, feel free to reach out to our support team. We're here to help!</p>\n" +
                        "        <p>Happy learning!</p>\n" +
                        "        <p>The Courses Platform Team</p>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>\n";
        mailer.send(Mail.withHtml(person.getEmail(), "Welcome to Course Applications", htmlBody));
    }

    public void sendCourseCandidatureStatusEmail(Person person, Course course, Registration registration) {
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Course Application Update</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f7f7f7;\n" +
                "            color: #333;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 40px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #007bff;\n" +
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            padding: 10px 20px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Course Application Update</h1>\n" +
                "        <p>Hello " + person.getFirstName() + ",</p>\n" +
                "        <p>We wanted to inform you about the status of your candidature.</p>\n" +
                "        <p>Your application for " + course.getName() + " has changed to " + registration.getStatus() + " .</p>\n" +
                "        <p>If you have any questions or need further assistance, feel free to contact us.</p>\n" +
                "        <p>Best regards,</p>\n" +
                "        <p>The Courses Platform Team</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        mailer.send(Mail.withHtml(person.getEmail(), "Course Application Status", htmlBody));
    }

    public void sendEmailWithCertificate(Person person, Course course) {
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Congratulations on Completing the Course!</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f7f7f7;\n" +
                "            color: #333;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 40px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center; \n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #007bff;\n" +
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            padding: 10px 20px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Congratulations on Completing the Course!</h1>\n" +
                "        <p>Hello " + person.getFirstName() + ",</p>\n" +
                "        <p>We're thrilled to inform you that you have successfully completed the course " + course.getName() + ".</p>\n" +
                "        <p>Congratulations on this achievement!</p>\n" +
                "        <p>Your certification is attached to this email. Please download and keep it for your records.</p>\n" +
                "        <p>If you have any questions or need further assistance, feel free to contact us.</p>\n" +
                "        <p>Best regards,</p>\n" +
                "        <p>The Courses Platform Team</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        mailer.send(Mail.withHtml(person.getEmail(), "Congratulations you have completed the course!", htmlBody).addAttachment("certificate.pdf", "certificate.pdf".getBytes(), "application/pdf"));
    }
}