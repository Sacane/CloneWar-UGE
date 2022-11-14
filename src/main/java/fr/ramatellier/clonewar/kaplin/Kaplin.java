package fr.ramatellier.clonewar.kaplin;

import fr.ramatellier.clonewar.instruction.Instruction;
import fr.ramatellier.clonewar.util.Hasher;

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
            if(rabinKarp(instruction.content(), elem.content())) {
                return true;
            }
        }

        return false;
    }

    public static int compareJarInstructions(List<Instruction> instructions1, List<Instruction> instructions2) {
        var nbClone = 0;

        for(var instruction: instructions1) {
            if(compareInstructionWithJarInstructions(instruction, instructions2)) {
                nbClone++;
            }
        }

        return (nbClone / instructions1.size()) * 100;
    }
}