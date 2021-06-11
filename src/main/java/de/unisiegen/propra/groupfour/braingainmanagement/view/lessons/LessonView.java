package de.unisiegen.propra.groupfour.braingainmanagement.view.lessons;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Lesson;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.LessonService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.subject.SubjectView;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Route(value = "lessons/:LessonID?/:action?(edit)", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Stunden")
public class LessonView extends Div implements BeforeEnterObserver {
    //TODO Grid Datenquelle ändern für spezifischen Tutor
    private final static String LESSON_ID = "LessonID";
    private final static String LESSON_EDIT_ROUTE_TEMPLATE = "lessons/%s/edit";
    private Grid<Lesson> grid = new Grid<>(Lesson.class, false);

    private DatePicker date;
    private IntegerField count;
    private Select<Customer> customer;
    private Select<Subject> subject;
    private Select<Tutor> tutor;

    /*
    private TextField prename;
    private TextField surname;
    private TextField phone;
    private TextField email;
    private TextField street;
    private TextField city;
    private TextField zipcode;
    private TextField bic;
    private TextField iban;

     */
    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;
    // TODO: get from Spring security session
    private Tutor tutorOBJECT;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");

    private Binder<Lesson> binder;

    private Lesson lesson;

    private final LessonService lessonService;

    public LessonView(@Autowired LessonService lessonService, @Autowired TutorService tutorService) {
        // TODO: FIX
        tutorOBJECT = tutorService.fetchAll().get(0);

        addClassNames("lesson-view", "flex", "flex-col", "h-full");
        this.lessonService = lessonService;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("date").setHeader("Datum").setAutoWidth(true);
        grid.addColumn("customer").setHeader("Schüler").setAutoWidth(true);
        grid.addColumn("subject").setHeader("Fach").setAutoWidth(true);
        grid.addColumn("count").setHeader("Stundenanzahl").setAutoWidth(true);

        /*grid.addColumn("phone").setHeader("Telefon").setAutoWidth(true);
        grid.addColumn("email").setHeader("Email").setAutoWidth(true);
        grid.addColumn("street").setHeader("Straße").setAutoWidth(true);
        grid.addColumn("city").setHeader("Stadt").setAutoWidth(true);
        grid.addColumn("zipcode").setHeader("PLZ").setAutoWidth(true);
        grid.addColumn("bic").setHeader("BIC").setAutoWidth(true);
        grid.addColumn("iban").setHeader("IBAN").setAutoWidth(true);

         */
        //grid.addColumn("occupation").setAutoWidth(true);

        // TODO: improve, idk what I have done
        grid.setDataProvider(DataProvider.fromCallbacks(query -> {
            query.getOffset();
            query.getLimit();
            return lessonService.fetchAll().stream();
        }, query -> lessonService.count()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(LESSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(LessonView.class);
            }
        });

        // Configure Form
        binder = new Binder<>(Lesson.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.lesson == null) {

                    this.lesson = new Lesson();
                }
                binder.writeBean(this.lesson);

                lessonService.update(this.lesson);
                clearForm();
                refreshGrid();
                Notification.show("Lesson details stored.");
                UI.getCurrent().navigate(LessonView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Lesson details.");
            }
        });
        delete.addClickListener(e -> {
            try {
                if (this.lesson == null) {
                    this.lesson = new Lesson();
                }
                binder.writeBean(this.lesson);
                lessonService.delete(this.lesson.getId());
                clearForm();
                refreshGrid();
                Notification.show("Lesson details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(LessonView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to delete the lesson details.");
            }




        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> lessonId = event.getRouteParameters().get(LESSON_ID);
        if (lessonId.isPresent()) {
            Optional<Lesson> lessonFromBackend = lessonService.get(UUID.fromString(lessonId.get()));
            if (lessonFromBackend.isPresent()) {
                populateForm(lessonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested lesson was not found, ID = %s", lessonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(SubjectView.class);
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
        tutor = new Select<Tutor>();
        tutor.setLabel("Tutor");
        tutor.setItems(tutorOBJECT);
        tutor.setValue(tutorOBJECT);
        date = new DatePicker("Datum");
        date.setValue(LocalDate.now());
        count = new IntegerField("Stundenanzahl");
        count.hasControls();
        count.setMin(0);

        customer = new Select<Customer>();
        customer.setLabel("Schüler");

        customer.setItems(tutorOBJECT.getCustomers());

        subject = new Select<Subject>();
        subject.setLabel("Fach");
        subject.setItems(tutorOBJECT.getSubjects());
        /*prename = new TextField("Vorname");
        surname = new TextField("Nachname");
        phone = new TextField("Telefon");
        email = new TextField("Email");
        street = new TextField("Straße");
        city = new TextField("Stadt");
        zipcode = new TextField("PLZ");
        bic = new TextField("BIC");
        iban = new TextField("IBAN");

         */


        //dateOfBirth = new DatePicker("Date Of Birth");
        //occupation = new TextField("Occupation");
        //important = new Checkbox("Important");
        //important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{date,customer,subject,count,tutor};

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

    private void populateForm(Lesson value) {
        this.lesson = value;
        binder.readBean(this.lesson);

    }


}
