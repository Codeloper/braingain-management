package de.unisiegen.propra.groupfour.braingainmanagement.view.subject;

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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.SubjectService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.customer.CustomerView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.tutor.TutorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.util.Optional;
import java.util.UUID;

@Route(value = "subject/:subjectID?/:action?(edit)", layout = MainView.class)
@PageTitle("Fächer")
public class SubjectView extends Div implements BeforeEnterObserver {
    private final String SUBJECT_ID = "subjectID";
    private final String SUBJECT_EDIT_ROUTE_TEMPLATE = "subject/%s/edit";

    private Grid<Subject> grid = new Grid<>(Subject.class, false);
    private TextField id;
    private IntegerField tutorFee;
    private IntegerField customerPrice;
    //private TextField tutorfee;
    //private TextField studentprice;
    //private TextField email;
    //private TextField phone;
    //private TextField contact;
    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");

    private Binder<Subject> binder;

    private Subject subject;

    private SubjectService subjectService;

    public SubjectView(@Autowired SubjectService subjectService) {
    addClassNames("fächer-view", "flex", "flex-col", "h-full");
        this.subjectService = subjectService;
    // Create UI
    SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    // Configure Grid
        grid.addColumn("id").setHeader("Bezeichnung").setAutoWidth(true);
        grid.addColumn("tutorFee").setHeader("Tutorenhonorar").setAutoWidth(true);
        grid.addColumn("customerPrice").setHeader("Kundenpreis").setAutoWidth(true);
    //grid.addColumn("tutorFee").setHeader("Tutorenhonorar").setAutoWidth(true);
    //grid.addColumn("customerPrice").setHeader("Schülerpreis").setAutoWidth(true);
    //grid.addColumn("email").setHeader("Email").setAutoWidth(true);
    //grid.addColumn("contact").setHeader("Kontakt").setAutoWidth(true);
    //grid.addColumn("dateOfBirth").setAutoWidth(true);
    //grid.addColumn("occupation").setAutoWidth(true);


        // TODO: improve, idk what I have done
        grid.setDataProvider(DataProvider.fromCallbacks(query -> {
            query.getOffset();
            query.getLimit();
            return subjectService.fetchAll().stream();
        }, query -> subjectService.count()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

    // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
        if (event.getValue() != null) {
            UI.getCurrent().navigate(String.format(SUBJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
        } else {
            clearForm();
            UI.getCurrent().navigate(CustomerView.class);
        }
    });

    // Configure Form
    binder = new Binder<>(Subject.class);

    // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
        clearForm();
        refreshGrid();
    });

        save.addClickListener(e -> {
        try {
            if (this.subject == null) {
                this.subject = new Subject();
            }
            binder.writeBean(this.subject);

            subjectService.update(this.subject);
            clearForm();
            refreshGrid();
            Notification.show("Fach details stored.");
            UI.getCurrent().navigate(SubjectView.class);
        } catch (ValidationException validationException) {
            Notification.show("An exception happened while trying to store the Fach details.");
        }
    });

        delete.addClickListener(e -> {
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




        });
}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> subjectId = event.getRouteParameters().get(SUBJECT_ID);
        if (subjectId.isPresent()) {
            Optional<Subject> subjectFromBackend = subjectService.get(subjectId.get());
            if (subjectFromBackend.isPresent()) {
                populateForm(subjectFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested Fach was not found, ID = %d", subjectId.get()), 3000,
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
        id= new TextField("Bezeichnung");
        tutorFee = new IntegerField("Tutorenhonorar");
        tutorFee.setHasControls(true);
        tutorFee.setMin(0);

        customerPrice = new IntegerField("Kundenpreis");
        customerPrice.setHasControls(true);
        customerPrice.setMin(0);
        //studentprice = new TextField("Schüelerpreis");
        //tutorfee = new TextField("Tutorenhonorar");
        //email = new TextField("Email");
        //phone = new TextField("Kontakt");
        //dateOfBirth = new DatePicker("Date Of Birth");
        //occupation = new TextField("Occupation");
        //important = new Checkbox("Important");
        //important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{id,tutorFee,customerPrice};

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

    private void populateForm(Subject value) {
        this.subject = value;
        binder.readBean(this.subject);

    }
}
