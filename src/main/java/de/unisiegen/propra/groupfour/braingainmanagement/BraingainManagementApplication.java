package de.unisiegen.propra.groupfour.braingainmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vaadin.artur.helpers.LaunchUtil;

@SpringBootApplication
public class BraingainManagementApplication {

	public static void main(String[] args) {
		LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(BraingainManagementApplication.class, args));
	}

}
