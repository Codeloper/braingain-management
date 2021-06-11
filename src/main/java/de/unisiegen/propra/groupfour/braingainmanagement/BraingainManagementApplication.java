package de.unisiegen.propra.groupfour.braingainmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.vaadin.artur.helpers.LaunchUtil;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class BraingainManagementApplication {

	public static void main(String[] args) {
		LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(BraingainManagementApplication.class, args));
	}

}
