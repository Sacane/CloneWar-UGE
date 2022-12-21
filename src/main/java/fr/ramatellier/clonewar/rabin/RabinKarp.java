package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.instruction.Instruction;

import java.util.List;

public class RabinKarp {
    private static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        for(var elem: instructions) {
            if(elem.hashValue() == instruction.hashValue() && elem.content().equals(instruction.content())) {
                return true;
            }
        }

        return false;
    }

    public static int compareJarInstructions(List<Instruction> instructionsJar1, List<Instruction> instructionsJar2) {
        var nbActualInstruction = 0;
        double nbInstruction = instructionsJar1.size();

        for(var instruction: instructionsJar1) {
            if(compareInstructionWithJarInstructions(instruction, instructionsJar2)) {
                nbActualInstruction++;
            }
        }

        return (int) ((nbActualInstruction / nbInstruction) * 100);
    }
}