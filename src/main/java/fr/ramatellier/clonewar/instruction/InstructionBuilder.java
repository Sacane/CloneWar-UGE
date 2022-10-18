package fr.ramatellier.clonewar.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InstructionBuilder {
    private final List<String> instructions;
    private StringBuilder actual_instruction;

    public InstructionBuilder() {
        instructions = new ArrayList<>();
        actual_instruction = new StringBuilder();
    }

    public void append(String str) {
        Objects.requireNonNull(str);

        actual_instruction.append(str);
        actual_instruction.append("\n");
    }

    public void endInstruction() {
        var instruction = actual_instruction.toString();
        if (!instructions.equals("")) {
            instructions.add(instruction);
        }
        actual_instruction = new StringBuilder();
    }

    public void addToDataBase() {
        System.out.println("Affichage du builder :");
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "NEW BLOC --- >\n" + instructions.stream().collect(Collectors.joining("NEW BLOC --- >\n"));
    }
}