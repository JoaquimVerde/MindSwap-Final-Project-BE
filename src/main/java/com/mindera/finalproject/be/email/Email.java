package com.mindera.finalproject.be.email;

import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.exception.email.EmailException;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.pdf.PdfException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.MailerName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

@ApplicationScoped
public class Email {

    @Inject
    @MailerName("outlook")
    Mailer mailer;

    public void sendWelcomeEmail(Person person) throws EmailGetTemplateException {
        String html = getTemplate("welcomeEmail.html");
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{loginUrl}}", "http://localhost:8080/login"); // TODO CHANGE URL TO PRODUCTION URL
        mailer.send(Mail.withHtml(person.getEmail(), "Welcome to Course Applications", html));
    }

    public void sendCourseCandidatureStatusEmail(Person person, Course course, Registration registration) throws EmailGetTemplateException {
        String html = getTemplate("statusEmail.html");
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{status}}", registration.getStatus());
        mailer.send(Mail.withHtml(person.getEmail(), "Course Application Status", html));
    }

    public void sendCourseInvoice(Person person, Course course) throws EmailGetTemplateException {
        String html = getTemplate("statusEmail.html");
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{courseName}}", course.getName());
        mailer.send(Mail.withHtml(person.getEmail(), "Invoice for course Enrollment", html));
    }

    public void sendEmailWithCertificate(Person person, Course course) throws EmailGetTemplateException {
        String html = getTemplate("certificateEmail.html");
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{finalDate}}", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        mailer.send(Mail.withHtml(person.getEmail(), "Congratulations you have completed the course!", html).addAttachment("certificate.pdf", "certificate.pdf".getBytes(), "application/pdf"));
    }


    private String getTemplate(String template) throws EmailGetTemplateException {
        String templateFilePath = "com/mindera/finalproject/be/html/" + template;
        String html;
        try {
            html = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getResource(templateFilePath)).toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new EmailGetTemplateException("Error reading HTML template file");
        }
        return html;
    }
}