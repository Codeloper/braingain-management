package de.unisiegen.propra.groupfour.braingainmanagement.view.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.User;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.UserRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.UserService;
import de.unisiegen.propra.groupfour.braingainmanagement.security.SecurityConfiguration;
import de.unisiegen.propra.groupfour.braingainmanagement.view.TutorLessonView.TutorLessonView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.customer.CustomerView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.invoice.InvoiceView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.lessons.LessonView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.statistic.StatisticView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.subject.SubjectView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.tutor.TutorView;
import de.unisiegen.propra.groupfour.braingainmanagement.view.user.UserView;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Braingain Management", shortName = "Braingain", enableInstallPrompt = true)
@Theme(themeFolder = "app", variant = Lumo.DARK)
public class MainView extends AppLayout {
    private Image logo;
    private final Tabs menu;

    public MainView() {
        logo = new Image("images/logo.png", "My App");
        logo.setId("logo");
        HorizontalLayout header = createHeader();
        menu = createMenuTabs();
        addToNavbar(createTopBar(header, menu));
    }

    private VerticalLayout createTopBar(HorizontalLayout header, Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.getThemeList().add("dark");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);



        layout.add(header);
        layout.add(menu);



        return layout;
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setId("header");
        header.add(logo);


        Avatar avatar = new Avatar();
        avatar.setName("Username");
        avatar.setId("avatar");
        Div tempDiv = new Div();
        tempDiv.add(avatar);

        tempDiv.addClickListener(e -> {
            com.vaadin.flow.component.dialog.Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();

            FormLayout formLayout = new FormLayout();





            /*
            Button dialogLogout = new Button("Ausloggen");
            dialogLogout.addClickListener(ev -> {

                dialog.close();
                UI.getCurrent().navigate("/logout");
                UI.getCurrent().na

            });
*/          //TODO Change Password input fields missing
            //formLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            String Username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson().getFullName();
            H2 label = new H2(Username);
            PasswordField pw1 = new PasswordField("Neues Passwort");
            PasswordField pw2 = new PasswordField("Passwort wiederholen");
            Button pwButton = new Button("Passwort ändern");
            pwButton.addClickListener(a -> {
                User us= ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

                if(pw1.getValue().equals(pw2.getValue())&&!pw1.getValue().equals("")) {
                    us.setPasswordHash(SecurityConfiguration.passwordEncoder.encode(pw1.getValue()));
                    userService.update(us);
                    dialog.close();
                }else{
                    Notification.show("Passwörter stimmen nicht überein!",3000,Notification.Position.BOTTOM_START);
                }
            });
            formLayout.add(label);
            formLayout.add(pw1);
            formLayout.add(pw2);
            formLayout.add(pwButton);
            //formLayout.add(logout);
            dialog.add(formLayout);
            dialog.setWidth("500px");
            dialog.setHeight("300px");
            dialog.open();
                //System.out.println("TEST");

        });


        header.add(tempDiv);

        Anchor logout = new Anchor("logout", "Log out");
        //Anchor logout = new Anchor("logout", "Log out");
        header.add(logout);



        return header;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.getStyle().set("max-width", "100%");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole() == User.Role.ADMIN ?
                new Tab[] {createTab("Schüler", CustomerView.class), createTab("Tutoren", TutorView.class), createTab("Fächer", SubjectView.class), createTab("Stunden", LessonView.class), createTab("Abrechnungen", InvoiceView.class), createTab("User", UserView.class),createTab("Statistik", StatisticView.class)}
                : new Tab[] {createTab("Stunden", TutorLessonView.class)};
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }
}
