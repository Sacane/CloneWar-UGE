package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.instruction.Instruction;

import java.io.IOException;
import java.util.List;

public class Kaplin {
    private static boolean rabinKarp(String s, String pattern) {
        var hashPattern = Hasher.hash(pattern);

        for(var i = 0; i < s.length() - pattern.length() + 1; i++) {
            var subS = s.substring(i, i + pattern.length() - 1);
            System.out.println(subS);
            var hashS = Hasher.hash(subS);
            if(hashS == hashPattern) {
                return true;
            }
        }

        return false;
    }

    public static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        for(var elem: instructions) {
            if(elem.hashValue() == instruction.hashValue() && elem.content().equals(instruction.content())) {
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

        return (nbActualInstruction / nbInstruction) * 100;
    }
}