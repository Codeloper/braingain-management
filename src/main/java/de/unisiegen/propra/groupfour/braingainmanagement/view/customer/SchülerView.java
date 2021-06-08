package de.unisiegen.propra.groupfour.braingainmanagement.view.customer;

import java.util.Optional;
import java.util.UUID;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.data.binder.Binder;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.CustomerService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.tutor.TutorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.textfield.TextField;

@Route(value = "customer/:PersonID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Schüler")
public class SchülerView extends Div implements BeforeEnterObserver {

    private final String CUSTOMER_ID = "personID";
    private final String CUSTOMER_EDIT_ROUTE_TEMPLATE = "customer/%d/edit";

    private Grid<Customer> grid = new Grid<>(Customer.class, false);

    private TextField prename;
    private TextField surname;
    private TextField phone;
    private TextField email;
    private TextField street;
    private TextField city;
    private TextField zipcode;
    private TextField invoiceStreet;
    private TextField invoiceCity;
    private TextField invoiceZipcode;
    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Customer> binder;

    private Customer customer;

    private CustomerService customerService;

    public SchülerView(@Autowired CustomerService customerService) {
        addClassNames("schüler-view", "flex", "flex-col", "h-full");
        this.customerService = customerService;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("prename").setHeader("Vorname").setAutoWidth(true);
        grid.addColumn("surname").setHeader("Nachname").setAutoWidth(true);
        //grid.addColumn("address").setHeader("Adresse").setAutoWidth(true);
        //grid.addColumn("email").setHeader("Email").setAutoWidth(true);
        //grid.addColumn("contact").setHeader("Kontakt").setAutoWidth(true);
        //grid.addColumn("dateOfBirth").setAutoWidth(true);
        //grid.addColumn("occupation").setAutoWidth(true);


        // TODO: improve, idk what I have done
        grid.setDataProvider(DataProvider.fromCallbacks(query -> {
            query.getOffset();
            query.getLimit();
            return customerService.fetchAll().stream();
        }, query -> customerService.count()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CUSTOMER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(SchülerView.class);
            }
        });

        // Configure Form
        binder = new Binder<>(Customer.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.customer == null) {
                    this.customer = new Customer();
                }
                binder.writeBean(this.customer);

                customerService.update(this.customer);
                clearForm();
                refreshGrid();
                Notification.show("Customer details stored.");
                UI.getCurrent().navigate(SchülerView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the customer details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> customerId = event.getRouteParameters().get(CUSTOMER_ID);
        if (customerId.isPresent()) {
            Optional<Customer> customerFromBackend = customerService.get(UUID.fromString(customerId.get()));
            if (customerFromBackend.isPresent()) {
                populateForm(customerFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested tutor was not found, ID = %d", customerId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TutorView.class);
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
        prename = new TextField("Vorname");
        surname = new TextField("Nachname");
        phone = new TextField("Telefon");
        email = new TextField("Email");
        street = new TextField("Straße");
        city = new TextField("Stadt");
        zipcode = new TextField("PLZ");
        invoiceStreet = new TextField("Rechnungsadresse");
        invoiceCity = new TextField("Rechnungsstadt");
        invoiceZipcode = new TextField("Rechnungs-PLZ");

        //dateOfBirth = new DatePicker("Date Of Birth");
        //occupation = new TextField("Occupation");
        //important = new Checkbox("Important");
        //important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{prename, surname, phone, email, street, city, zipcode,invoiceStreet,invoiceCity,invoiceZipcode};

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
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
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

    private void populateForm(Customer value) {
        this.customer = value;
        binder.readBean(this.customer);

    }
}
