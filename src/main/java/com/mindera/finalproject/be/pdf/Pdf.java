package com.mindera.finalproject.be.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
import com.mindera.finalproject.be.exception.pdf.PdfException;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@ApplicationScoped
public class Pdf {

    public byte[] generateInvoicePdf(Person person, Course course) throws PdfCreateException {
        String html = getTemplate("InvoiceTemplate.html");
        html = html.replace("{{invoiceNumber}}", Year.now().getValue() + "/" + person.getSK().substring(7, 10) + "_" + course.getSK().substring(7, 10));
        html = html.replace("{{currentDate}}", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)));
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{coursePrice}}", String.valueOf(course.getPrice()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        return outputStream.toByteArray();
    }

    public byte[] generateCertificatePdf(Person person, Course course) throws PdfCreateException {
        String html = getTemplate("CertificateTemplate.html");
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{finalDate}}", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        return outputStream.toByteArray();
    }

    private String getTemplate(String template) throws PdfCreateException {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader("/home/fguedes/Documents/GitHub/final-project-be/src/main/java/com/mindera/finalproject/be/html/" + template));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            throw new PdfCreateException("Error reading PDF template file");
        }
        return contentBuilder.toString();
    }
}
