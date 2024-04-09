package com.mindera.finalproject.be.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.exception.pdf.PdfException;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@ApplicationScoped
public class Pdf {

    private Integer invoiceCounter = 1;

    private Integer certificateCounter = 1;

    public void generateInvoicePdf(Person person, Course course) throws PdfException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Course Enrollment Invoice</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
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
                "\n" +
                "<div class=\"invoice\">\n" +
                "    <div class=\"invoice-header\">\n" +
                "        <h2>Course Enrollment Invoice</h2>\n" +
                "    </div>\n" +
                "    <div class=\"invoice-body\">\n" +
                "        <p><strong>Invoice Number:</strong> #INV-" + invoiceCounter + "</p>\n" +
                "        <p><strong>Date:</strong> " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)) + " </p>\n" +
                "        <p><strong>Student Name:</strong>" + person.getFirstName() + " " + person.getLastName() + "</p>\n" +
                "        <p><strong>Course:</strong>" + course.getName() + "</p>\n" +
                "        <table class=\"invoice-table\">\n" +
                "            <thead>\n" +
                "                <tr>\n" +
                "                    <th>Description</th>\n" +
                "                    <th>Quantity</th>\n" +
                "                    <th>Unit Price</th>\n" +
                "                    <th>Total</th>\n" +
                "                </tr>\n" +
                "            </thead>\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td>Course Fee</td>\n" +
                "                    <td>1</td>\n" +
                "                    <td>" + course.getPrice() + "</td>\n" +
                "                    <td>" + course.getPrice() + "</td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "        <div class=\"invoice-total\">\n" +
                "            <p><strong>Total:</strong>" + course.getPrice() + "/p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
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

    public void generateCertificatePdf() throws PdfException {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Conclusion Certificate</title>\n" +
                "    <style>\n" +
                "        @page {\n" +
                "            size: landscape;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
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
                "\n" +
                "<div class=\"certificate\">\n" +
                "    <div class=\"certificate-header\">\n" +
                "        <h2>Conclusion Certificate</h2>\n" +
                "    </div>\n" +
                "    <div class=\"certificate-content\">\n" +
                "        <p class=\"certificate-name\">Certificate of Completion</p>\n" +
                "        <p class=\"certificate-details\">This is to certify that</p>\n" +
                "        <p class=\"certificate-details\"><strong>John Doe</strong></p>\n" +
                "        <p class=\"certificate-details\">has successfully completed the course</p>\n" +
                "        <p class=\"certificate-details\"><strong>Introduction to Programming</strong></p>\n" +
                "        <p class=\"certificate-details\">Date of Completion: April 9, 2024</p>\n" +
                "    </div>\n" +
                "    <div class=\"certificate-signature\">\n" +
                "        <p>Signature: </p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, outputStream);
        byte[] pdfBytes = outputStream.toByteArray();
        String pdfFilePath = "/home/fguedes/Documents/GitHub/final-project-be/src/main/java/com/mindera/finalproject/be/pdf/certificate-00" + certificateCounter + ".pdf";
        invoiceCounter++;
        try {
            FileOutputStream fos = new FileOutputStream(pdfFilePath);
            fos.write(pdfBytes);
            fos.close();
        } catch (IOException e) {
            throw new PdfException("Error creating PDF file");
        }
    }

}
