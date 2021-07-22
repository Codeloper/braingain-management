package de.unisiegen.propra.groupfour.braingainmanagement.view.statistic;

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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.LessonService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.subject.SubjectView;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Route(value = "statistic/:StatisticID?/:action?(edit)", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Statistik")
public class StatisticView  extends Div implements BeforeEnterObserver {

    private final static String STATISTIC_ID = "StatisticID";
    private final static String STATISTIC_EDIT_ROUTE_TEMPLATE = "statistic/%s/edit";
    private Grid<Statistic> grid = new Grid<>(Statistic.class, false);

    private DatePicker dateStart;
    private DatePicker dateUntil;






    //private DatePicker dateOfBirth;
    //private TextField occupation;
    //private Checkbox important;
    // TODO: get from Spring security session


    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Statistik anzeigen");


   // private Binder<> binder;

   // private Lesson lesson;

   private final LessonService lessonService;
    private final TutorService tutorService;

    public StatisticView(@Autowired LessonService lessonService, @Autowired TutorService tutorService) {


        addClassNames("statistic-view", "flex", "flex-col", "h-full");
        this.lessonService = lessonService;
        this.tutorService = tutorService;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        List<Statistic> statistics = new ArrayList<Statistic>();
        List<Lesson> lessons;
        List<Tutor> tutors = tutorService.fetchAll();

        for(Tutor t : tutors){
            double expense = 0;
            double profit = 0;
            double sum = 0;
            lessons = lessonService.fetchAllByTutor(t);
            for (Lesson l : lessons){
                expense += l.getSubject().getTutorFee();
                profit += l.getSubject().getCustomerPrice();


            }
            sum = profit-expense;
            statistics.add(new Statistic(t,expense,profit,sum));
        }

        // Configure Grid
        grid.setItems(statistics);

        //grid.setColumns("tutor", "Ausgaben", "Einnahmen", "Summe");

        grid.addColumn("tutor").setHeader("Tutor").setAutoWidth(true);
        grid.addColumn("expenses").setHeader("Ausgaben").setAutoWidth(true);
        grid.addColumn("profits").setHeader("Einnahmen").setAutoWidth(true);
        grid.addColumn("sum").setHeader("Summe").setAutoWidth(true);
/*
        grid.setDataProvider(DataProvider.fromCallbacks(query -> {
            query.getOffset();
            query.getLimit();
            return lessonService.fetchAll().stream();
        }, query -> lessonService.count()));
        */

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        /*
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(LESSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(LessonView.class);
            }
        });
*/
        // Configure Form
        //binder = new Binder<>(Lesson.class);

        // Bind fields. This where you'd define e.g. validation rules

        //.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {

                clearForm();
                refreshGrid();
                Notification.show("Watch Statistics above");
                UI.getCurrent().navigate(StatisticView.class);
            } catch (Exception e1) {
                Notification.show("An exception happened while trying to provide Statistics.");
            }
        });




    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //Optional<String> lessonId = event.getRouteParameters().get(LESSON_ID);
        //if (lessonId.isPresent()) {
          //  Optional<Lesson> lessonFromBackend = lessonService.get(UUID.fromString(lessonId.get()));
          //  if (lessonFromBackend.isPresent()) {
                //populateForm(lessonFromBackend.get());
           // } else {
           //     Notification.show(
            //            String.format("The requested lesson was not found, ID = %s", lessonId.get()), 3000,
             //           Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
             //   refreshGrid();
               // event.forwardTo(StatisticView.class);
           // }
       // }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        /*Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        dateStart = new DatePicker("von Datum");
        dateStart.setValue(LocalDate.now());

        dateUntil = new DatePicker("bis Datum");
        dateUntil.setValue(LocalDate.now());
        Component[] fields = new Component[]{dateStart, dateUntil};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
        */

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
        H1 Label = new H1("Tutorenstatistik");
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(Label);
        wrapper.add(grid);
        H1 Label1 = new H1("Fächerstatistik");
        wrapper.add(Label1);
    }

    private void refreshGrid() {
        //grid.select(null);
        //grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Lesson value) {
        //this.lesson = value;
       // binder.readBean(this.lesson);

    }


}
