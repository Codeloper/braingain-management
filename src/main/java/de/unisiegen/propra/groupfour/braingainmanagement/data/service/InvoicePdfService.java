package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Invoice;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class InvoicePdfService {

    private final static String INVOICE_FILE_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "invoice-%s.pdf";

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public void createPdf(Invoice invoice) throws IOException {
        try (final OutputStream os = new FileOutputStream(String.format(INVOICE_FILE_PATH, invoice.getId()))) {
            final Context context = new Context();

            context.setVariable("invoice", invoice);

            final String htmlBody = springTemplateEngine.process(invoice.getRecipient() instanceof Customer ? "invoice.html" : "paycheck.html", context);

            final PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withW3cDocument(new W3CDom().fromJsoup(Jsoup.parse(htmlBody)), null);
            builder.toStream(os);

            builder.run();
        }
    }

}
