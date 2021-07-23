package de.unisiegen.propra.groupfour.braingainmanagement.view.tutor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.CustomerService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.SubjectService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Optional;
import java.util.UUID;

@Route(value = "tutors/:PersonID?/:action?(edit)", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Tutoren")
public class TutorView extends Div implements BeforeEnterObserver {

    private final static String TUTOR_ID = "PersonID";
    private final static String TUTOR_EDIT_ROUTE_TEMPLATE = "tutors/%s/edit";

    private Grid<Tutor> grid = new Grid<>(Tutor.class, false);

    private TextField prename;
    private TextField surname;
    private TextField phone;
    private TextField email;
    private TextField street;
    private TextField city;
    private TextField zipcode;
    private TextField bic;
    private TextField iban;
    private TextField annotation;
    private MultiselectComboBox<Subject> subjects;
    private MultiselectComboBox<Customer> customers;
    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");

    private Binder<Tutor> binder;

    private Tutor tutor;

    private final TutorService tutorService;


    private SubjectService subjectService;
    private CustomerService customerService;

    public TutorView(@Autowired TutorService tutorService,@Autowired SubjectService subjectService,@Autowired CustomerService customerService) {
        addClassNames("tutoren-view", "flex", "flex-col", "h-full");
        this.tutorService = tutorService;
        this.subjectService= subjectService;
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
        grid.addColumn("annotation").setHeader("Anmerkung").setAutoWidth(true);
        grid.addColumn("phone").setHeader("Telefon").setAutoWidth(true);
        grid.addColumn("email").setHeader("Email").setAutoWidth(true);
        grid.addColumn("street").setHeader("Straße").setAutoWidth(true);
        grid.addColumn("city").setHeader("Stadt").setAutoWidth(true);
        grid.addColumn("zipcode").setHeader("PLZ").setAutoWidth(true);
        grid.addColumn("bic").setHeader("BIC").setAutoWidth(true);
        grid.addColumn("iban").setHeader("IBAN").setAutoWidth(true);
        //grid.addColumn("occupation").setAutoWidth(true);

        // TODO: improve, idk what I have done
        grid.setDataProvider(DataProvider.fromCallbacks(query -> {
            query.getOffset();
            query.getLimit();
            return tutorService.fetchAll().stream();
        }, query -> tutorService.count()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TUTOR_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TutorView.class);
            }
        });

        // Configure Form
        binder = new Binder<>(Tutor.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.tutor == null) {
                    this.tutor = new Tutor();
                }
                binder.writeBean(this.tutor);
                this.tutor.setAnnotation("");
                tutorService.update(this.tutor);
                clearForm();
                refreshGrid();
                Notification.show("Tutoren details stored.");
                UI.getCurrent().navigate(TutorView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Tutoren details.");
            }catch(DataIntegrityViolationException a){
                Notification.show("Zu diesem Tutor bestehen Beziehungen in anderen Tabellen. Löschen nicht möglich!",3000,Notification.Position.BOTTOM_START);
            }
        });
        delete.addClickListener(e -> {
            try {
                if (this.tutor == null) {
                    this.tutor = new Tutor();
                }
                binder.writeBean(this.tutor);
                tutorService.delete(this.tutor.getId());
                clearForm();
                refreshGrid();
                Notification.show("Customer details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(TutorView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to delete the customer details.");
            }




        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> tutorId = event.getRouteParameters().get(TUTOR_ID);
        if (tutorId.isPresent()) {

            Optional<Tutor> tutorFromBackend = tutorService.get(UUID.fromString(tutorId.get()));
            if (tutorFromBackend.isPresent()) {
                populateForm(tutorFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested tutor was not found, ID = %s", tutorId.get()), 3000,
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
        annotation = new TextField("Anmerkung");
        phone = new TextField("Telefon");
        email = new TextField("Email");
        street = new TextField("Straße");
        city = new TextField("Stadt");
        zipcode = new TextField("PLZ");
        bic = new TextField("BIC");
        iban = new TextField("IBAN");
        subjects = new MultiselectComboBox<Subject>();
        subjects.setLabel("Fächer");
        subjects.setItems(subjectService.fetchAll());
        customers = new MultiselectComboBox<Customer>();
        customers.setLabel("Schüler");
        customers.setItems(customerService.fetchAll());


        //dateOfBirth = new DatePicker("Date Of Birth");
        //occupation = new TextField("Occupation");
        //important = new Checkbox("Important");
        //important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{prename, surname, annotation, phone, email, street, city, zipcode, bic, iban,subjects,customers};

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
        buttonLayout.add(save, cancel,delete);
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

    private void populateForm(Tutor value) {
        this.tutor = value;
        binder.readBean(this.tutor);

    }
}
