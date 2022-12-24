package fr.ramatellier.clonewar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class CloneWarApplication {
	private static final Logger LOGGER = Logger.getLogger(CloneWarApplication.class.getName());
	public static void main(String[] args) {
		SpringApplication.run(CloneWarApplication.class, args);
	}
}
