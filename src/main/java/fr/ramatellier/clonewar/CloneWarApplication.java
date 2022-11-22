package fr.ramatellier.clonewar;

import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
public class CloneWarApplication {
	private static final Logger LOGGER = Logger.getLogger(CloneWarApplication.class.getName());
	public static void main(String[] args) {
		try {
			InstructionBuilder.buildInstructionFromJar("TestJar.jar");
		} catch (IOException e) {
			throw new AssertionError(e);
		}

		// SpringApplication.run(CloneWarApplication.class, args);
	}

}
