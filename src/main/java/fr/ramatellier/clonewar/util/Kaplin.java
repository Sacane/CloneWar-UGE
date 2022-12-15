package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.instruction.Instruction;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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

    private static boolean rabinKarp(String s, String pattern) {
        var hashPattern = Hasher.hash(pattern);

        for(var i = 0; i < s.length() - pattern.length() + 1; i++) {
            var subS = s.substring(i, i + pattern.length() - 1);
            var hashS = Hasher.hash(subS);
            if(hashS == hashPattern && contentLength(subS) == contentLength(pattern)) {
                return true;
            }
        }

        return false;
    }

    public static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        for(var elem: instructions) {
            if(elem.hashValue() == instruction.hashValue()) {
                return true;
            }
        }

        return false;
    }

    public static int compareJarInstructions(List<Instruction> instructionsJar1, List<Instruction> instructionsJar2) throws IOException {
        var nbActualInstruction = 0;
        var nbInstruction = instructionsJar1.size();

        for(var instruction: instructionsJar1) {
            if(compareInstructionWithJarInstructions(instruction, instructionsJar2)) {
                nbActualInstruction++;
            }
        }

        System.out.println(nbActualInstruction + " " + nbInstruction);

        var content1 = "LOAD 100";
        var content2 = "LOAD 100";

        System.out.println(rabinKarp(content1, content2));

        return (nbActualInstruction / nbInstruction) * 100;
    }
}