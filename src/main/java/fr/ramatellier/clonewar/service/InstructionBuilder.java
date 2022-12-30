package fr.ramatellier.clonewar.service;

import fr.ramatellier.clonewar.persistence.Instruction.Instruction;
import fr.ramatellier.clonewar.service.util.AsmParser;
import fr.ramatellier.clonewar.service.util.Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class InstructionBuilder {
    private final ArrayList<Instruction> instructions = new ArrayList<>();

    private StringBuilder actualInstruction = new StringBuilder();

    private int actualFirstLine;

    private boolean hasFirstLine;

    private int order = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(InstructionBuilder.class);

    public InstructionBuilder(String name) {
        Objects.requireNonNull(name);
    }

    /**
     * Getter for hasFirstLine field
     * @return True if the current instruction builder already have a first line or false if it doesn't
     */
    public boolean hasFirstLine() {
        return hasFirstLine;
    }

    /**
     * Getter for instructions field
     * @return The list of instructions that have been built by the instruction builder
     */
    public ArrayList<Instruction> instructions() {
        return instructions;
    }

    /**
     * Will add a String to the current instruction that the builder is reading and go back to a new line
     * @param str The String that we want to append to the current instruction
     */
    public void append(String str) {
        Objects.requireNonNull(str);

        actualInstruction.append(str);
        actualInstruction.append("\n");
    }

    /**
     * Method to give to the builder the number of the first line of the instruction
     * @param line The number of the first line
     */
    public void firstLine(int line) {
        actualFirstLine = line;
        hasFirstLine = true;
    }

    /**
     * Method to say to the builder that the current instruction is finish, so we can add it to the list of instructions
     * @param filename The name of the file that contains the current instruction
     */
    public void endInstruction(String filename) {
        var instruction = actualInstruction.toString();
        if (!instruction.equals("")) {
            instructions.add(new Instruction(filename, actualFirstLine, actualInstruction.toString(), order));
            order++;
        }
        actualInstruction = new StringBuilder();
        hasFirstLine = false;
    }

    /**
     * This method will calculate the hash of each content with a specified window with the rabin karp algorithm
     * @param content The array of content that we want to cut with a window and calculate the specified hash
     * @param window The window is an int to specified how much content we will put together
     * @param hash The hash value that correspond to each content of the previous array
     * @return A Map of String and Long, each group of content with his hash score
     */
    private static Map<String, Long> instructionToScore(String[] content, int window, long[] hash) {
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

    /**
     * Method to calculate the hash that correspond to each content and then call instructionToScore
     * @param content The array of content that we want to cut with a window and calculate the specified hash
     * @param window The window is an int to specified how much content we will put together
     * @return A Map of String and Long, each group of content with his hash score
     */
    static Map<String, Long> cutStringWithWindow(String[] content, int window) {
        var hash = new long[content.length];
        if(content.length < window) {
            return new HashMap<String, Long>();
        }
        for(var i = 0; i < content.length; i++) {
            hash[i] = Hash.hash(content[i]);
        }
        return instructionToScore(content, window, hash);
    }

    /**
     * Method to get all the instructions of a jar with a specified window to group them
     * @param jarName Name of the jar that we want to get the instructions
     * @param bytes The array of bytes that represent the content of the .class files
     * @return The list of instructions that correspond to the content of the jar
     * @throws IOException Because AsmParser.getInstructionsFromJar throw IOException
     */
    public static ArrayList<Instruction> buildInstructionFromJar(String jarName, byte[] bytes) throws IOException {
        LOGGER.info("start building instructions...");
        var window = 3;
        var list = new ArrayList<Instruction>();
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