package fr.ramatellier.clonewar.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InstructionBuilder {
    private final ArrayList<Instruction> instructions;
    private StringBuilder actualInstruction;
    private int actualFirstLine;
    private boolean hasFirstLine;

    public InstructionBuilder() {
        instructions = new ArrayList<>();
        actualInstruction = new StringBuilder();
    }

    public boolean hasFirstLine() {
        return hasFirstLine;
    }

    public void append(String str) {
        Objects.requireNonNull(str);

        actualInstruction.append(str);
        actualInstruction.append("\n");
    }

    public void firstLine(int line) {
        actualFirstLine = line;
        hasFirstLine = true;
    }

    public void endInstruction() {
        var instruction = actualInstruction.toString();
        if (!instructions.equals("")) {
            instructions.add(new Instruction(actualFirstLine, actualInstruction.toString()));
        }
        actualInstruction = new StringBuilder();
        hasFirstLine = false;
    }

    public void addToDataBase() {
        System.out.println("Affichage des instructions :");
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "NEW INSTRUCTION --- >\n" + instructions.stream().map(Instruction::toString).collect(Collectors.joining("NEW INSTRUCTION --- >\n"));
    }
}