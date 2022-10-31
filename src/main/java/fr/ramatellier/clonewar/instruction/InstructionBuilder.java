package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.artifact.ArtifactController;
import fr.ramatellier.clonewar.asm.Asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class InstructionBuilder {
    private final String filename;
    private final ArrayList<Instruction> instructions;
    private StringBuilder actualInstruction;
    private int actualFirstLine;
    private boolean hasFirstLine;

    public InstructionBuilder(String name) {
        filename = name;
        instructions = new ArrayList<>();
        actualInstruction = new StringBuilder();
    }

    public String filename() {
        return filename;
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
        if (!instruction.equals("")) {
            instructions.add(new Instruction(filename, actualFirstLine, actualInstruction.toString(), 0));
        }
        actualInstruction = new StringBuilder();
        hasFirstLine = false;
    }

    public static ArrayList<Instruction> buildInstructionFromJar(String jarName) throws IOException {
        var builder = new InstructionBuilder(jarName);

        Asm.addInstructionsFromJar(builder);

        return builder.instructions;
    }

    @Override
    public String toString() {
        return "NEW INSTRUCTION --- >\n" + instructions.stream().map(Instruction::toString).collect(Collectors.joining("NEW INSTRUCTION --- >\n"));
    }
}