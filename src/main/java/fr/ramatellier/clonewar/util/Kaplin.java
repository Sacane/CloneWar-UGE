package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.instruction.Instruction;

import java.util.List;

public class Kaplin {
    public static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        for(var elem: instructions) {
            if(elem.hashValue() == instruction.hashValue() && elem.content().equals(instruction.content())) {
                return true;
            }
        }

        return false;
    }

    public static int compareJarInstructions(List<Instruction> instructionsJar1, List<Instruction> instructionsJar2) {
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