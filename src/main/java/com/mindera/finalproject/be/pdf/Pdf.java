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
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
                "    <title>Course Enrollment Invoice</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background-color: #f2f2f2;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .invoice {\n" +
                "            width: 80%;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .invoice-header {\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            padding: 10px;\n" +
                "            border-top-left-radius: 10px;\n" +
                "            border-top-right-radius: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .invoice-body {\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .invoice-table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "        }\n" +
                "        .invoice-table th, .invoice-table td {\n" +
                "            border: 1px solid #ddd;\n" +
                "            padding: 8px;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .invoice-total {\n" +
                "            margin-top: 20px;\n" +
                "            text-align: right;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"invoice\">\n" +
                "    <div class=\"invoice-header\">\n" +
                "        <h2>Course Enrollment Invoice</h2>\n" +
                "    </div>\n" +
                "    <div class=\"invoice-body\">\n" +
                "        <p><strong>Invoice Number:</strong> #INV-{{invoiceNumber}}</p>\n" +
                "        <p><strong>Date:</strong> {{currentDate}}</p>\n" +
                "        <p><strong>Student Name:</strong> {{studentName}}</p>\n" +
                "        <p><strong>Course:</strong> {{courseName}}</p>\n" +
                "        <table class=\"invoice-table\">\n" +
                "            <thead>\n" +
                "            <tr>\n" +
                "                <th>Description</th>\n" +
                "                <th>Quantity</th>\n" +
                "                <th>Unit Price</th>\n" +
                "                <th>Total</th>\n" +
                "            </tr>\n" +
                "            </thead>\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td>Course Fee</td>\n" +
                "                <td>1</td>\n" +
                "                <td>{{coursePrice}}€</td>\n" +
                "                <td>{{coursePrice}}€</td>\n" +
                "            </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "        <div class=\"invoice-total\">\n" +
                "            <p><strong>Total:</strong> {{coursePrice}}€</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
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
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
                "    <title>Conclusion Certificate</title>\n" +
                "    <style>\n" +
                "        @page {\n" +
                "            size: landscape;\n" +
                "        }\n" +
                "        body {\n" +
                "            background-color: #fff;\n" +
                "            color: #000;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .certificate {\n" +
                "            width: 80%;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .certificate-header {\n" +
                "            background-color: #000;\n" +
                "            color: #fff;\n" +
                "            padding: 10px;\n" +
                "            border-top-left-radius: 10px;\n" +
                "            border-top-right-radius: 10px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .certificate-content {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .certificate-name {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .certificate-details {\n" +
                "            font-size: 18px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .certificate-signature {\n" +
                "            font-size: 18px;\n" +
                "            font-style: italic;\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"certificate\">\n" +
                "    <div class=\"certificate-header\">\n" +
                "        <h2>Conclusion Certificate</h2>\n" +
                "    </div>\n" +
                "    <div class=\"certificate-content\">\n" +
                "        <p class=\"certificate-name\">Certificate of Completion</p>\n" +
                "        <p class=\"certificate-details\">This is to certify that</p>\n" +
                "        <p class=\"certificate-details\"><strong>{{studentName}}</strong></p>\n" +
                "        <p class=\"certificate-details\">has successfully completed the course</p>\n" +
                "        <p class=\"certificate-details\"><strong>{{courseName}}</strong></p>\n" +
                "        <p class=\"certificate-details\">Date of Completion: {{finalDate}}</p>\n" +
                "    </div>\n" +
                "    <div class=\"certificate-signature\">\n" +
                "        <p>Signature: The Acodemy Team</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
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
            BufferedReader in = new BufferedReader(new FileReader("src/main/java/com/mindera/finalproject/be/html/" + template));
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
