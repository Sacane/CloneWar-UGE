package fr.ramatellier.clonewar.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InstructionBuilder {
    private List<String> instructions;
    private StringBuilder actual_instruction;

    public void IntructionBuilder() {
        instructions = new ArrayList<String>();
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
            instructions.add(actual_instruction.toString());
        }
        actual_instruction = new StringBuilder();
    }

    public void addToDataBase() {
    }

    @Override
    public String toString() {
        return instructions.stream().collect(Collectors.joining("NEW BLOC --- >\n"));
    }
}