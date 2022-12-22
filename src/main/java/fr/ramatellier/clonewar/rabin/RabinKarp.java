package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.instruction.Instruction;

import java.util.Comparator;
import java.util.List;

public final class RabinKarp {
    private static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        for(var elem: instructions) {
            if(elem.hashValue() == instruction.hashValue() && elem.content().equals(instruction.content())) {
                return true;
            }
        }

        return false;
    }

    public static int onInstructions(List<Instruction> instructionsJar1, List<Instruction> instructionsJar2) {
        var nbActualInstruction = 0;
        double nbInstruction = instructionsJar1.size();

        instructionsJar1 = instructionsJar1.stream().sorted(Comparator.comparing(Instruction::hashValue)).toList();
        instructionsJar2 = instructionsJar2.stream().sorted(Comparator.comparing(Instruction::hashValue)).toList();
        var i = 0;
        var j = 0;

        while(i < nbInstruction) {
            var instruction1 = instructionsJar1.get(i);
            var instruction2 = instructionsJar2.get(j);

            if(instruction1.hashValue() < instruction2.hashValue()) {
                i++;
            }
            else if(instruction1.hashValue() > instruction2.hashValue()) {
                if(j == instructionsJar2.size() - 1)
                    break;
                j++;
            }
            else {
                if(instruction1.content().equals(instruction2.content()))
                    nbActualInstruction++;
                i++;
            }
        }

        /*for(var instruction: instructionsJar1) {
            if(compareInstructionWithJarInstructions(instruction, instructionsJar2)) {
                nbActualInstruction++;
            }
        }*/

        return (int) ((nbActualInstruction / nbInstruction) * 100);
    }
}