package de.unisiegen.propra.groupfour.braingainmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.vaadin.artur.helpers.LaunchUtil;

import java.io.IOException;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class BraingainManagementApplication {

	public static void main(String[] args) throws IOException {
		LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(BraingainManagementApplication.class, args));
		// only for local run:
		// SpringApplication.run(BraingainManagementApplication.class, args);
		// Desktop.getDesktop().open(new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Braingain.app"));
	}

}
