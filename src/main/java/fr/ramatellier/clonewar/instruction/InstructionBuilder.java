package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.AsmParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TODO all the logic here has to be refract because a builder should take the filename of the
 */
public class InstructionBuilder {
    private final ArrayList<Instruction> instructions = new ArrayList<>();
    private StringBuilder actualInstruction = new StringBuilder();
    private int actualFirstLine;
    private boolean hasFirstLine;
    private int order = 0;

    public InstructionBuilder(String name) {
        Objects.requireNonNull(name);
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

    public void endInstruction(String filename) {
        var instruction = actualInstruction.toString();
        if (!instruction.equals("")) {
            instructions.add(new Instruction(filename, actualFirstLine, actualInstruction.toString(), order));
            order++;
        }
        actualInstruction = new StringBuilder();
        hasFirstLine = false;
    }

    private static List<String> cutStringWithWindow(String[] content, int window) {
        var contentList = new ArrayList<String>();
        for(var i = 0; i < content.length - window + 1; i++) {
            var newContent = new StringBuilder();
            for(var j = i; j < i + window; j++) {
                newContent.append(content[j]);
            }
            contentList.add(newContent.toString());
        }
        return contentList;
    }

    public static ArrayList<Instruction> buildInstructionFromJar(String jarName, byte[] bytes) throws IOException {
        var window = 3;
        var list = new ArrayList<Instruction>();

        var instructions = AsmParser.getInstructionsFromJar(jarName, bytes);
        for(var instruction: instructions) {
            var content = cutStringWithWindow(instruction.content().split("\n"), window);
            for(var elem: content) {
                list.add(new Instruction(instruction.filename(), instruction.getLineNumberStart(), elem, instruction.order()));
            }
        }
        /*var content = cutStringWithWindow(AsmParser.getInstructionsFromJar(jarName, bytes).get(0).content().split("\n"), window);
        for(var elem: content) {
            list.add(new Instruction(jarName, 0, elem, 0));
        }*/


        return list;
    }

    @Override
    public String toString() {
        return "NEW INSTRUCTION --- >\n" + instructions.stream().map(Instruction::toString).collect(Collectors.joining("NEW INSTRUCTION --- >\n"));
    }
}