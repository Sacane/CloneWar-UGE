package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.AsmParser;
import fr.ramatellier.clonewar.util.Hasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class InstructionBuilder {
    private final ArrayList<Instruction> instructions = new ArrayList<>();
    private StringBuilder actualInstruction = new StringBuilder();

    private static final Logger LOGGER = LoggerFactory.getLogger(InstructionBuilder.class);
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


    private static Map<String, Long> instructionToScore(String[] content, int window, long[] hash){
        var contentList = new HashMap<String, Long>();
        var hashScore = 0L;
        for(var i = 0; i < window; i++) {
            hashScore += hash[i];
        }
        for(var i = 0; i < content.length - window + 1; i++) {
            var newContent = new StringBuilder();
            for(var j = i; j < i + window; j++) {
                newContent.append(content[j]);
            }
            if(i != 0) {
                hashScore = hashScore - hash[i - 1] + hash[i + window - 1];
            }
            contentList.put(newContent.toString(), hashScore);
        }
        return contentList;
    }
    static Map<String, Long> cutStringWithWindow(String[] content, int window) {
        var contentList = new HashMap<String, Long>();
        var hash = new long[content.length];
        if(content.length < window) {
            return contentList;
        }
        for(var i = 0; i < content.length; i++) {
            hash[i] = Hasher.hash(content[i]);
        }
        return instructionToScore(content, window, hash);
    }

    public static ArrayList<Instruction> buildInstructionFromJar(String jarName, byte[] bytes) throws IOException {
        LOGGER.info("start building instructions...");
        var window = 3;
        var list = new ArrayList<Instruction>();
        LOGGER.info("Get instruction from jar");
        var instructions = AsmParser.getInstructionsFromJar(jarName, bytes);
        LOGGER.info("start indexing (using rabin karp)");
        for(var instruction: instructions) {
            LOGGER.info("Treating a new instruction");
            var content = cutStringWithWindow(instruction.content().split("\n"), window);
            for(var element: content.entrySet()) {
                list.add(new Instruction(instruction.filename(), instruction.getLineNumberStart(), element.getKey(), instruction.order()));
            }
        }

        return list;
    }

    @Override
    public String toString() {
        return "NEW INSTRUCTION --- >\n" + instructions.stream().map(Instruction::toString).collect(Collectors.joining("NEW INSTRUCTION --- >\n"));
    }
}