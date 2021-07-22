package de.unisiegen.propra.groupfour.braingainmanagement.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.User;
import de.unisiegen.propra.groupfour.braingainmanagement.view.login.LoginView;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component 
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> { 
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::authenticateNavigation);
		});
	}

	private void authenticateNavigation(BeforeEnterEvent event) {
		if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
			if(SecurityUtils.isUserLoggedIn()) {
				event.rerouteToError(NotFoundException.class);
			} else {
				event.rerouteTo(LoginView.class);
			}
		}
	}

}