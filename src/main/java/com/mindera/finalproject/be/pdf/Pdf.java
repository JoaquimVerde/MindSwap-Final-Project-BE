package com.mindera.finalproject.be.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.pdf.PdfException;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

@ApplicationScoped
public class Pdf {

    private Integer invoiceCounter = 1;

    private Integer certificateCounter = 1;

    public void generateInvoicePdf(Person person, Course course) throws PdfException {
        String html = getTemplate("InvoiceTemplate.html");
        html = html.replace("{{invoiceNumber}}", String.valueOf(invoiceCounter));
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{coursePrice}}", String.valueOf(course.getPrice()));
        html = html.replace("{{courseDate}}", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        byte[] pdfBytes = outputStream.toByteArray();
        String pdfFilePath = "/home/fguedes/Documents/GitHub/final-project-be/src/main/java/com/mindera/finalproject/be/pdf/invoice-00" + invoiceCounter + ".pdf";
        invoiceCounter++;
        try {
            FileOutputStream fos = new FileOutputStream(pdfFilePath);
            fos.write(pdfBytes);
            fos.close();
        } catch (IOException e) {
            throw new PdfException("Error creating PDF file");
        }
    }


    public void generateCertificatePdf(Person person, Course course) throws PdfException {
        String html = getTemplate("CertificateTemplate.html");
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{finalDate}}", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        byte[] pdfBytes = outputStream.toByteArray();
        String pdfFilePath = "/home/fguedes/Documents/GitHub/final-project-be/src/main/java/com/mindera/finalproject/be/pdf/certificate-00" + certificateCounter + ".pdf";
        certificateCounter++;
        try {
            FileOutputStream fos = new FileOutputStream(pdfFilePath);
            fos.write(pdfBytes);
            fos.close();
        } catch (IOException e) {
            throw new PdfException("Error creating PDF file");
        }
    }


    private String getTemplate(String template) throws PdfException {
        String templateFilePath = "com/mindera/finalproject/be/html/" + template;
        String html;
        try {
            html = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getResource(templateFilePath)).toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new PdfException("Error reading HTML template file");
        }
        return html;
    }
}
