package com.mindera.finalproject.be.email;

import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
import com.mindera.finalproject.be.pdf.Pdf;

import com.mindera.finalproject.be.s3.S3Service;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.MailerName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@ApplicationScoped
public class Email {

    @Inject
    @MailerName("outlook")
    Mailer mailer;

    @Inject
    S3Service s3Service;

    @Inject
    Pdf pdf;


    public void sendWelcomeEmail(Person person) throws EmailGetTemplateException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
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
                "        .list {\n" +
                "            text-align: left;\n" +
                "            margin-left: 40px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <h1>Welcome to Acodemy Platform!</h1>\n" +
                "    <p>Hello {{firstName}},</p>\n" +
                "    <p>We're thrilled to have you join our community! You're now part of an exciting journey of learning and growth.</p>\n" +
                "    <p>Here are a few things you can do:</p>\n" +
                "    <ul class=\"list\">\n" +
                "        <li>Explore our courses and find the perfect one for you.</li>\n" +
                "        <li>Start learning and unlock new opportunities!</li>\n" +
                "    </ul>\n" +
                "    <p>Ready to get started? Click the button below to log in to your account:</p>\n" +
                "    <p><a class=\"btn\" href=\"{{loginUrl}}\">Log In</a></p>\n" +
                "    <p>If you have any questions or need assistance, feel free to reach out to our support team. We're here to help!</p>\n" +
                "    <p>Happy learning!</p>\n" +
                "    <p>The Acodemy Team</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{loginUrl}}", "https://fe-deployment.d1c8xxfduy22sd.amplifyapp.com/"); // TODO CHANGE URL TO PRODUCTION URL
        mailer.send(Mail.withHtml(person.getEmail(), "Welcome to Course Applications", html));
    }

    public void sendCourseCandidatureStatusEmail(Person person, Course course, Registration registration) throws EmailGetTemplateException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
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
                "<div class=\"container\">\n" +
                "    <h1>Course Application Update</h1>\n" +
                "    <p>Hello {{firstName}},</p>\n" +
                "    <p>We wanted to inform you about the status of your candidature.</p>\n" +
                "    <p>Your application for {{courseName}} has changed to {{status}}.</p>\n" +
                "    <p>If you have any questions or need further assistance, feel free to contact us.</p>\n" +
                "    <p>Best regards,</p>\n" +
                "    <p>The Acodemy Team</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{status}}", registration.getStatus());
        mailer.send(Mail.withHtml(person.getEmail(), "Course Application Status", html));
    }

    public void sendCourseInvoice(Person person, Course course) throws EmailGetTemplateException, PdfCreateException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
                "    <title>Invoice for Course Enrollment</title>\n" +
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
                "<div class=\"container\">\n" +
                "    <h1>Invoice for Course Enrollment</h1>\n" +
                "    <p>Dear {{firstName}},</p>\n" +
                "    <p>Congratulations! We are pleased to inform you that you have been accepted into the course \"{{courseName}}\".</p>\n" +
                "    <p>As a part of your enrollment process, please find attached the invoice for the course fees. Kindly review the\n" +
                "        details and proceed with the payment to secure your spot in the course.</p>\n" +
                "    <p>If you have any queries regarding the invoice or need assistance with the payment process, feel free to contact\n" +
                "        us. We're here to help!</p>\n" +
                "    <p>Thank you for choosing our platform for your learning journey.</p>\n" +
                "    <p>Best regards,</p>\n" +
                "    <p>The Acodemy Team</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{courseName}}", course.getName());
        byte[] pdfBytes = s3Service.uploadInvoice(person, course);
        mailer.send(Mail.withHtml(person.getEmail(), "Invoice for course Enrollment", html).addAttachment("invoice" + course.getSK().substring(7, 11) + "_" + person.getSK().substring(7, 11) + ".pdf", pdfBytes, "application/pdf"));
    }

    public void sendEmailWithCertificate(Person person, Course course) throws EmailGetTemplateException, PdfCreateException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
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
                "<div class=\"container\">\n" +
                "    <h1>Congratulations on Completing the Course!</h1>\n" +
                "    <p>Hello {{firstName}},</p>\n" +
                "    <p>We're thrilled to inform you that you have successfully completed the course {{courseName}}.</p>\n" +
                "    <p>Congratulations on this achievement!</p>\n" +
                "    <p>Your certification is attached to this email. Please download and keep it for your records.</p>\n" +
                "    <p>If you have any questions or need further assistance, feel free to contact us.</p>\n" +
                "    <p>Best regards,</p>\n" +
                "    <p>The Acodemy Team</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{finalDate}}", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        byte[] pdfBytes = s3Service.uploadCertificate(person, course);
        mailer.send(Mail.withHtml(person.getEmail(), "Congratulations you have completed the course!", html).addAttachment("certificate" + "_" + course.getName() + "_" + person.getSK().substring(7, 11) + ".pdf", pdfBytes, "application/pdf"));
    }

    private String getTemplate(String template) throws EmailGetTemplateException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("com/mindera/finalproject/be/html/" + template);
            if(in == null) {
                throw new EmailGetTemplateException("Error reading HTML template file");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = reader.readLine()) != null) {
                contentBuilder.append(str);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("deu merda");
            throw new EmailGetTemplateException("Error reading HTML template file");
        }
        return contentBuilder.toString();
    }
}