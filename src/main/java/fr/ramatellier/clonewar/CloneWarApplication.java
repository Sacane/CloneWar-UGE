package fr.ramatellier.clonewar;

import fr.ramatellier.clonewar.asm.Asm;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CloneWarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloneWarApplication.class, args);
	}

}
