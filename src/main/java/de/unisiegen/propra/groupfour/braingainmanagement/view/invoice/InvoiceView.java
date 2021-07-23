package de.unisiegen.propra.groupfour.braingainmanagement.view.invoice;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Person;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Invoice;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.*;
import de.unisiegen.propra.groupfour.braingainmanagement.util.exception.LessonAlreadyInvoicedException;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Route(value = "invoice/:invoiceID?/:action?(edit)", layout = MainView.class)
@PageTitle("Abrechnungen")
public class InvoiceView extends Div implements BeforeEnterObserver {
    private final static String INVOICEID = "invoiceID";
    private final static String INVOICE_EDIT_ROUTE_TEMPLATE = "invoice/%s/edit";
    private Grid<Invoice> grid = new Grid<>(Invoice.class, false);

    private DatePicker datestart;
    private DatePicker dateuntil;
    private Select<Person> recipient;

    private Button cancel = new Button("Abbrechen");
    private Button generate = new Button("Abrechnung generieren");

    private Anchor generatePdfAnchor = new Anchor(new StreamResource("Rechnung.pdf", () -> {
        try {
            return new FileInputStream("invoice.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }), "");
    private Button generatePdfButton = new Button("PDF erstellen");
    //private Button delete = new Button("Löschen");

    private Binder<Invoice> binder;

    private Invoice invoice;

    private InvoiceService invoiceService;

    private InvoicePdfService invoicePdfService;

    private LessonService lessonService;
    private List<Person> personList;
    public InvoiceView(@Autowired InvoiceService invoiceService, @Autowired LessonService lessonService, @Autowired PersonService personService, @Autowired InvoicePdfService invoicePdfService) {
        personList = personService.fetchAll();
        addClassNames("customerinvoice-view", "flex", "flex-col", "h-full");
        this.invoiceService = invoiceService;
        this.invoicePdfService = invoicePdfService;
        this.lessonService = lessonService;

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id").setHeader("Rechnungsnummer").setAutoWidth(true);
        grid.addColumn("date").setHeader("Rechnungsdatum").setAutoWidth(true);
        grid.addColumn("recipient").setHeader("Rechnungsempfänger").setAutoWidth(true);

        grid.setItems(invoiceService.fetchAll());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(INVOICE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(InvoiceView.class);
            }
        });

        // Configure Form
        binder = new Binder<>(Invoice.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        generate.addClickListener(e -> {
            try {
                invoice = invoiceService.createInvoice(recipient.getValue(), datestart.getValue(), dateuntil.getValue());
                binder.writeBean(this.invoice);
                invoiceService.update(this.invoice);
                clearForm();
                refreshGrid();
                Notification.show("Rechnung details stored.");
                UI.getCurrent().navigate(InvoiceView.class);
                UI.getCurrent().getPage().reload();
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Rechnung details.");
            }
            catch(LessonAlreadyInvoicedException exception){
                Notification.show("Mindestens eine Stunde im ausgewählten Zeitraum wurde bereits abgerechnet");

            }
        });

        generatePdfAnchor.getElement().setAttribute("download", true);
        generatePdfAnchor.add(generatePdfButton);

        generatePdfButton.addClickListener(e -> {
            if(!grid.getSelectedItems().iterator().hasNext()) {
                Notification.show("Bitte wählen Sie eine Rechnung aus");
                return;
            }
            if (invoice != null){
                try {
                   invoicePdfService.createPdf(invoice);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

       /* delete.addClickListener(e -> {
            try {
                if (this.subject == null) {
                    this.subject = new Subject();
                }
                binder.writeBean(this.subject);
                subjectService.delete(this.subject.getId());
                clearForm();
                refreshGrid();
                Notification.show("Subject details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(TutorView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to delete the customer details.");
            }
        })*/
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> invoiceID = event.getRouteParameters().get(INVOICEID);
        if (invoiceID.isPresent()) {
            Optional<Invoice> invoiceFromBackend = invoiceService.get(invoiceID.get());
            if (invoiceFromBackend.isPresent()) {
                populateForm(invoiceFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested invoice was not found, ID = %s", invoiceID.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(InvoiceView.class);
            }
        }
    }


    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();

        recipient = new Select<>();
        recipient.setLabel("Schüler/Tutor");
        recipient.setItems(personList.stream().map(c -> c));

        datestart = new DatePicker("Abrechnungszeitraum ab");
        datestart.setValue(LocalDate.now());
        dateuntil = new DatePicker("Abrechnungszeitraum bis");
        dateuntil.setValue(LocalDate.now());

        Component[] fields = new Component[]{datestart,dateuntil, recipient};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        generate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(generate, cancel, generatePdfAnchor);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Invoice value) {
        this.invoice = value;
        binder.readBean(this.invoice);
    }
}
