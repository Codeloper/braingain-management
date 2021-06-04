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
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

@Route(value = "tutors/:PersonID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Tutoren")
public class TutorView extends Div implements BeforeEnterObserver {

    private final static String TUTOR_ID = "PersonID";
    private final static String TUTOR_EDIT_ROUTE_TEMPLATE = "tutors/%s/edit";

    private Grid<Tutor> grid = new Grid<>(Tutor.class, false);

    private TextField prename;
    private TextField surname;
    private TextField street;
    private TextField email;
    private TextField phone;
    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Tutor> binder;

    private Tutor tutor;

    private final TutorService tutorService;

    public TutorView(@Autowired TutorService tutorService) {
        addClassNames("tutoren-view", "flex", "flex-col", "h-full");
        this.tutorService = tutorService;
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
                    System.out.println("ALAAAAAAARM");
                    this.tutor = new Tutor();
                }
                binder.writeBean(this.tutor);

                tutorService.update(this.tutor);
                clearForm();
                refreshGrid();
                Notification.show("Tutoren details stored.");
                UI.getCurrent().navigate(TutorView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Tutoren details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> tutorId = event.getRouteParameters().get(TUTOR_ID);
        if (tutorId.isPresent()) {
            System.out.println("TUTOR ID: " + tutorId.get());
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
        street = new TextField("Adresse");
        email = new TextField("Email");
        phone = new TextField("Kontakt");
        //dateOfBirth = new DatePicker("Date Of Birth");
        //occupation = new TextField("Occupation");
        //important = new Checkbox("Important");
        //important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{prename, surname, street, email, phone};

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

    private void populateForm(Tutor value) {
        this.tutor = value;
        binder.readBean(this.tutor);

    }
}
