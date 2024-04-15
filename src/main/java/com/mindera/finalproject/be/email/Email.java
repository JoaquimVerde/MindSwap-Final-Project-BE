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
        String html = getTemplate("WelcomeEmail.html");
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{loginUrl}}", "https://fe-deployment.d1c8xxfduy22sd.amplifyapp.com/login"); // TODO CHANGE URL TO PRODUCTION URL
        mailer.send(Mail.withHtml(person.getEmail(), "Welcome to Course Applications", html));
    }

    public void sendCourseCandidatureStatusEmail(Person person, Course course, Registration registration) throws EmailGetTemplateException {
        String html = getTemplate("StatusEmail.html");
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{status}}", registration.getStatus());
        mailer.send(Mail.withHtml(person.getEmail(), "Course Application Status", html));
    }

    public void sendCourseInvoice(Person person, Course course) throws EmailGetTemplateException, PdfCreateException {
        String html = getTemplate("InvoiceEmail.html");
        html = html.replace("{{firstName}}", person.getFirstName());
        html = html.replace("{{courseName}}", course.getName());
        byte[] pdfBytes = s3Service.uploadInvoice(person, course);
        mailer.send(Mail.withHtml(person.getEmail(), "Invoice for course Enrollment", html).addAttachment("invoice" + course.getSK().substring(7, 11) + "_" + person.getSK().substring(7, 11) + ".pdf", pdfBytes, "application/pdf"));
    }

    public void sendEmailWithCertificate(Person person, Course course) throws EmailGetTemplateException, PdfCreateException {
        String html = getTemplate("CertificateEmail.html");
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{finalDate}}", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        byte[] pdfBytes = s3Service.uploadCertificate(person, course);
        mailer.send(Mail.withHtml(person.getEmail(), "Congratulations you have completed the course!", html).addAttachment("certificate" + "_" + course.getName() + "_" + person.getSK().substring(7, 11) + ".pdf", pdfBytes, "application/pdf"));
    }

    private String getTemplate(String template) throws EmailGetTemplateException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader("https://github.com/Mindswap-6th-Edition-Final/final-project-be/blob/0faad262b0e4759b1f08928160c3f250fea91ff6/src/main/java/com/mindera/finalproject/be/html/" + template));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("deu merda");
            throw new EmailGetTemplateException("Error reading HTML template file");
        }
        return contentBuilder.toString();
    }
}