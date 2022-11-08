package fr.ramatellier.clonewar.kaplin;

import fr.ramatellier.clonewar.instruction.Instruction;

import java.util.List;
import java.util.regex.Pattern;

public class Kaplin {
    private static int contentLength(String text) {
        var matcher = Pattern.compile("\n").matcher(text);
        int occur = 0;

        while(matcher.find()) {
            occur++;
        }
        return occur;
    }

    public static int compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        var score = 0;
        var scorePerInstruction = 100 / instructions.size();

        for(var elem: instructions) {
            if(instruction.hashValue() == elem.hashValue() && contentLength(instruction.content()) == contentLength(elem.content())) {
                score += scorePerInstruction;
            }
        }

        return score;
    }
}