package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.AsmParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TODO all the logic here has to be refract because a builder should take the filename of the
 */
public class InstructionBuilder {
    private final String filename;
    private final ArrayList<Instruction> instructions = new ArrayList<>();
    private StringBuilder actualInstruction = new StringBuilder();
    private int actualFirstLine;
    private boolean hasFirstLine;

    public InstructionBuilder(String name) {
        Objects.requireNonNull(name);
        filename = name;
    }

    public String filename() {
        return filename;
    }

    public boolean hasFirstLine() {
        return hasFirstLine;
    }

    public ArrayList<Instruction> instructions() {
        return instructions;
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
        var fenetre = 3;
        var list = new ArrayList<Instruction>();

        var content = AsmParser.addInstructionsFromJar(jarName).get(0).content().split("\n");

        for(var i = 0; i < content.length - fenetre + 1; i++) {
            var newChaine = "";
            for(var j = i; j < i + fenetre; j++) {
                newChaine += content[j];
            }
            System.out.println(newChaine);
        }

        return list;
    }

    @Override
    public String toString() {
        return "NEW INSTRUCTION --- >\n" + instructions.stream().map(Instruction::toString).collect(Collectors.joining("NEW INSTRUCTION --- >\n"));
    }
}