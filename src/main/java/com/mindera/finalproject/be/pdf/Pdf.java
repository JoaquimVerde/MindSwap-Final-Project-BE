package com.mindera.finalproject.be.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
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

    public void generateInvoicePdf(Person person, Course course) throws PdfCreateException {
        String html = getTemplate("InvoiceTemplate.html");
        html = html.replace("{{invoiceNumber}}", String.valueOf(invoiceCounter));
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{coursePrice}}", String.valueOf(course.getPrice()));
        html = html.replace("{{courseDate}}", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        byte[] pdfBytes = outputStream.toByteArray();
        int randomNumber = (int) (Math.random() * 1000);
        String pdfFilePath = "/home/fguedes/Documents/GitHub/final-project-be/src/main/java/com/mindera/finalproject/be/pdf/inv" + LocalDate.now().format(DateTimeFormatter.ofPattern("MMyyyy", Locale.ENGLISH)) + person.getSK() + ".pdf";
        invoiceCounter++;
        createPdf(pdfFilePath, pdfBytes);
    }

    public void generateCertificatePdf(Person person, Course course) throws PdfCreateException {
        String html = getTemplate("CertificateTemplate.html");
        html = html.replace("{{studentName}}", person.getFirstName() + " " + person.getLastName());
        html = html.replace("{{courseName}}", course.getName());
        html = html.replace("{{finalDate}}", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        byte[] pdfBytes = outputStream.toByteArray();
        String pdfFilePath = "/home/fguedes/Documents/GitHub/final-project-be/src/main/java/com/mindera/finalproject/be/pdf/certificate-00" + certificateCounter + ".pdf";
        certificateCounter++;
        createPdf(pdfFilePath, pdfBytes);
    }

    private String getTemplate(String template) throws PdfCreateException {
        String templateFilePath = "com/mindera/finalproject/be/html/" + template;
        String html;
        try {
            html = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getResource(templateFilePath)).toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new PdfCreateException("Error reading HTML template file");
        }
        return html;
    }

    private void createPdf(String pdfFilePath, byte[] pdfBytes) throws PdfCreateException {
        try {
            FileOutputStream fos = new FileOutputStream(pdfFilePath);
            fos.write(pdfBytes);
            fos.close();
        } catch (IOException e) {
            throw new PdfCreateException("Error creating PDF file");
        }
    }
}
