package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Invoice;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class InvoicePdfService {

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    private void createPdf(boolean customer, Invoice invoice) throws IOException {
        try (final OutputStream os = new FileOutputStream(customer ? "customerInvoice.pdf" : "tutorInvoice.pdf")) {
            final Context context = new Context();
            context.setVariable("invoice", invoice);

            final String htmlBody = springTemplateEngine.process(customer ? "invoice.html" : "paycheck.html", context);

            final PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withW3cDocument(new W3CDom().fromJsoup(Jsoup.parse(htmlBody)), null);
            builder.toStream(os);

            builder.run();
        }
    }

    public void createCustomerInvoice(Invoice invoice) throws IOException {
        createPdf(true, invoice);
    }

    public void createTutorInvoice(Invoice invoice) throws IOException {
        createPdf(false, invoice);
    }

}
