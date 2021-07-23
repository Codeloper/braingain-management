package de.unisiegen.propra.groupfour.braingainmanagement.view.customer;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.User;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.CustomerSubjectRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.CustomerService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.SubjectService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.TutorLessonView.TutorLessonView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.invoice.InvoiceView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.lessons.LessonView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.subject.SubjectView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.tutor.TutorView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.user.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.gatanaso.MultiselectComboBox;

@Route(value = "customer/:personID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Schüler")
public class CustomerView extends Div implements BeforeEnterObserver {

    private final String CUSTOMER_ID = "personID";
    private final String CUSTOMER_EDIT_ROUTE_TEMPLATE = "customer/%s/edit";

    private Grid<Customer> grid = new Grid<>(Customer.class, false);

    private TextField prename;
    private TextField surname;
    private TextField phone;
    private EmailField email;
    private TextField street;
    private TextField city;
    private TextField zipcode;
    private TextField invoiceStreet;
    private TextField invoiceCity;
    private TextField invoiceZipcode;
    private TextField annotation;
    //private NativeSelect subjects;
    //private MultiselectComboBox<CustomerSubject> subjects;
    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button addSubject = new Button("Fach hinzufügen");
    private Button deleteSubject = new Button("Fach Löschen");

    private Binder<Customer> binder;

    private Customer customer;

    private CustomerService customerService;
    private SubjectService subjectService;

    private CustomerSubjectRepository customerSubjectRepository;

    public CustomerView(@Autowired CustomerService customerService, @Autowired SubjectService subjectService, @Autowired CustomerSubjectRepository customerSubjectRepository) {
        addClassNames("schüler-view", "flex", "flex-col", "h-full");

        /*
        if(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole() == User.Role.ADMIN ){

        }else{
            UI.getCurrent().navigate(CustomerView.class);
        }
*/


        this.customerService = customerService;
        this.subjectService= subjectService;
        this.customerSubjectRepository = customerSubjectRepository;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("prename").setHeader("Vorname").setAutoWidth(true);
        grid.addColumn("surname").setHeader("Nachname").setAutoWidth(true);
        grid.addColumn("phone").setHeader("Telefon").setAutoWidth(true);
        grid.addColumn("email").setHeader("Email").setAutoWidth(true);
        grid.addColumn("street").setHeader("Straße").setAutoWidth(true);
        grid.addColumn("city").setHeader("Stadt").setAutoWidth(true);
        grid.addColumn("zipcode").setHeader("PLZ").setAutoWidth(true);
        grid.addColumn("invoiceStreet").setHeader("Rechnungsadresse").setAutoWidth(true);
        grid.addColumn("invoiceCity").setHeader("Rechnungsstraße").setAutoWidth(true);
        grid.addColumn("invoiceZipcode").setHeader("Rechnungs-PLZ").setAutoWidth(true);
        grid.addColumn("annotation").setHeader("Bemerkung").setAutoWidth(true);
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
                UI.getCurrent().navigate(CustomerView.class);
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
                binder.forField(email).withValidator(new EmailValidator("Keine valide Email-Adresse")).bind(Customer::getEmail, Customer::setEmail);
                binder.forField(phone).withValidator(phone -> phone.matches("\\+?[0-9 /]+"), "Keine valide Handynummer").bind(Customer::getPhone, Customer::setPhone);

                binder.writeBean(this.customer);
                customerService.update(this.customer);
                clearForm();
                refreshGrid();
                Notification.show("Customer details stored.");
                UI.getCurrent().navigate(CustomerView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the customer details.");
            }
        });

        delete.addClickListener(e -> {
            try {
                if (this.customer == null) {
                    this.customer = new Customer();
                }
                binder.writeBean(this.customer);
                customerService.delete(this.customer.getId());
                clearForm();
                refreshGrid();
                Notification.show("Customer details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(CustomerView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to delete the customer details.");
            }catch(DataIntegrityViolationException a){
                Notification.show("Zu diesem Schüler bestehen Beziehungen in anderen Tabellen. Löschen nicht möglich!",3000,Notification.Position.BOTTOM_START);
            }




        });

            addSubject.addClickListener(e-> {

            if(!grid.getSelectedItems().iterator().hasNext()) {
                Notification.show("Bitte wählen Sie einen Schüler aus");
                return;
            }
            try {
                if (this.customer == null) {
                    this.customer = new Customer();
                }
                binder.writeBean(this.customer);
                Customer customerTemp = customerService.get(((Customer) grid.getSelectedItems().toArray()[0]).getId()).get();
                com.vaadin.flow.component.dialog.Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();

                FormLayout formLayout = new FormLayout();
                Select<Subject> dialogSubject = new Select<Subject>();
                dialogSubject.setLabel("Fach");
                dialogSubject.setItems(subjectService.fetchAll());
                IntegerField dialogInteger = new IntegerField("Kontingent");
                dialogInteger.setHasControls(true);
                //dialogInteger.setLabel("");
                Button dialogAdd = new Button("Hinzufügen");
                dialogAdd.addClickListener(ev -> {
                    //TODO eingaben überprüfen
                    //this.customerService.get(customerTemp.getId()).addSubject(dialogSubject.getValue(),dialogInteger.getValue())
                    customerSubjectRepository.save(new CustomerSubject(customerTemp, dialogSubject.getValue(), dialogInteger.getValue()));
                    dialog.close();
                    clearForm();
                    refreshGrid();
                    Notification.show("Customer details stored.");
                    UI.getCurrent().navigate(CustomerView.class);
                    Notification.show("Added "+dialogSubject.getValue().toString()+": "+dialogInteger.getValue().toString()+" to "+customerTemp.getFullName(),3000,Notification.Position.BOTTOM_START);


                });
                Button cancelButton = new Button("Cancel", event -> {

                    dialog.close();
                });
                //dialog.add(new Text("Close me with the esc-key or an outside click"));
                formLayout.add(dialogSubject);
                formLayout.add(dialogInteger);
                formLayout.add(dialogAdd);
                formLayout.add(cancelButton);
                dialog.add(formLayout);
                dialog.setWidth("500px");
                dialog.setHeight("300px");
                dialog.open();
                clearForm();
                refreshGrid();
                //Notification.show("Customer details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(CustomerView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to add the customersubject details.");
            }

        });

        deleteSubject.addClickListener(e->{
            if(!grid.getSelectedItems().iterator().hasNext()) {
                Notification.show("Bitte wählen Sie einen Schüler aus");
                return;
            }
            try {
                if (this.customer == null) {
                    this.customer = new Customer();
                }
                binder.writeBean(this.customer);
                Customer customerTemp =  customerService.get(((Customer) grid.getSelectedItems().toArray()[0]).getId()).get();//(Customer) grid.getSelectedItems().toArray()[0];
               // Notification.show("Open Dialog for customer: "+customerTemp.getFullName(),3000,Notification.Position.BOTTOM_START);
                customerSubjectRepository.findAllByCustomerEquals(customerTemp).toArray();
                com.vaadin.flow.component.dialog.Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();

                FormLayout formLayout = new FormLayout();
                Select<CustomerSubject> dialogSubject = new Select<CustomerSubject>();
                dialogSubject.setLabel("Fach");
                //TODO fix list and binary type in database
                dialogSubject.setItems(customerTemp.getSubjects());
                //IntegerField dialogInteger = new IntegerField("Kontingent");
                //dialogInteger.hasControls();
                //dialogInteger.setLabel("");
                Button dialogDelete = new Button("Löschen");
                dialogDelete.addClickListener(ev -> {

                if(dialogSubject.getValue()==null) {
                    Notification.show("Select a Subject to be deleted",3000,Notification.Position.BOTTOM_START);
                    return;

                }
                    //Notification.show(dialogSubject.getValue().getSubject().toString(),3000,Notification.Position.BOTTOM_START);
                    //customerTemp.deleteSubject(dialogSubject.getValue());
                    customerSubjectRepository.delete(dialogSubject.getValue());
                    dialog.close();
                    clearForm();
                    refreshGrid();
                    Notification.show("deleted "+dialogSubject.getValue().toString()+" from "+customerTemp.getFullName(),3000,Notification.Position.BOTTOM_START);
                    UI.getCurrent().navigate(CustomerView.class);


                });
                Button cancelButton = new Button("Cancel", event -> {

                    dialog.close();
                });
                //dialog.add(new Text("Close me with the esc-key or an outside click"));
                formLayout.add(dialogSubject);

                formLayout.add(dialogDelete);
                formLayout.add(cancelButton);
                dialog.add(formLayout);
                dialog.setWidth("500px");
                dialog.setHeight("300px");
                dialog.open();
                clearForm();
                refreshGrid();
                //Notification.show("Customer details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(CustomerView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to delete the customersubject details.");
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
        email = new EmailField("Email");
        street = new TextField("Straße");
        city = new TextField("Stadt");
        zipcode = new TextField("PLZ");
        invoiceStreet = new TextField("Rechnungsadresse");
        invoiceCity = new TextField("Rechnungsstadt");
        invoiceZipcode = new TextField("Rechnungs-PLZ");
        annotation = new TextField("Bemerkung");

        Component[] fields = new Component[]{prename, surname, phone, email, street, city, zipcode,invoiceStreet,invoiceCity,invoiceZipcode,annotation};

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
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addSubject.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteSubject.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(save, cancel,delete,addSubject,deleteSubject);
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
