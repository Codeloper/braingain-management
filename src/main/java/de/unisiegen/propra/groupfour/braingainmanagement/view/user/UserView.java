package de.unisiegen.propra.groupfour.braingainmanagement.view.user;

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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.*;
import de.unisiegen.propra.groupfour.braingainmanagement.security.SecurityConfiguration;
import de.unisiegen.propra.groupfour.braingainmanagement.view.main.MainView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.tutor.TutorView;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Optional;
import java.util.UUID;

@Route(value = "users/:UserID?/:action?(edit)", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
@PageTitle("User")
public class UserView extends Div implements BeforeEnterObserver {
    private final static String USER_ID = "UserID";
    private final static String USER_EDIT_ROUTE_TEMPLATE = "users/%s/edit";

    private Grid<User> grid = new Grid<>(User.class, false);


    private TextField email;
    private TextField password;
    private Select<Person> tutor;


    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");

    private Binder<User> binder;

    private User user;

    private final UserService userService;

    private final TutorService tutorService;

    public UserView(@Autowired UserService userService, @Autowired TutorService tutorService) {
        addClassNames("user-view", "flex", "flex-col", "h-full");
        this.userService = userService;
        this.tutorService = tutorService;
        //this.useService= subjectService;
        //this.customerService = customerService;
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid

        grid.addColumn("email").setHeader("Email").setAutoWidth(true);
        grid.addColumn("person").setHeader("Tutor").setAutoWidth(true);
        //grid.addColumn("occupation").setAutoWidth(true);

        // TODO: improve, idk what I have done
        grid.setDataProvider(DataProvider.fromCallbacks(query -> {
            query.getOffset();
            query.getLimit();
            return userService.fetchAll().stream();
        }, query -> userService.count()));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(USER_EDIT_ROUTE_TEMPLATE, event.getValue().getEmail()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TutorView.class);
            }
        });

        // Configure Form
        binder = new Binder<>(User.class);

        // Bind fields. This where you'd define e.g. validation rules

        //binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            binder.forField(email).bind(User::getEmail, User::setEmail);
            binder.forField(tutor).bind(User::getPerson, User::setPerson);
            binder.forField(password).bind(User::getPasswordHash, (user, password) -> user.setPasswordHash(SecurityConfiguration.passwordEncoder.encode(password)));
            try {
                if (this.user == null) {
                    this.user = new User();
                }

                binder.writeBean(this.user);
                //this.user.setPasswordHash(passwordEncoder.encode("password.getValue()"));
                //this.user.setAnnotation("");
                this.user.setRole(User.Role.TUTOR);
                userService.update(this.user);
                clearForm();
                refreshGrid();
                Notification.show("User details stored.");
                UI.getCurrent().navigate(UserView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the Tutoren details.");
            }
        });
        delete.addClickListener(e -> {
            try {
                if (this.user == null) {
                    this.user = new User();
                }
                binder.writeBean(this.user);
                userService.delete(this.user.getEmail());
                clearForm();
                refreshGrid();
                Notification.show("User details deleted.");
                //this.customer=null;
                UI.getCurrent().navigate(UserView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to delete the customer details.");
            }




        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> userId = event.getRouteParameters().get(USER_ID);
        if (userId.isPresent()) {

            Optional<User> userFromBackend = userService.get(userId.get());
            if (userFromBackend.isPresent()) {
                populateForm(userFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested tutor was not found, ID = %s", userId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(UserView.class);
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

        email = new TextField("Email");
        tutor = new Select<>();
        tutor.setLabel("Tutor");
        tutor.setItems(tutorService.fetchAll().stream().map(t -> (Person) t));
        password = new TextField("Passwort");


        //dateOfBirth = new DatePicker("Date Of Birth");
        //occupation = new TextField("Occupation");
        //important = new Checkbox("Important");
        //important.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{email, tutor, password};

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

    private void populateForm(User value) {
        this.user = value;
        binder.readBean(this.user);

    }
}
