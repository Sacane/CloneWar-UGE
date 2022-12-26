package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.instruction.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class RabinKarp {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabinKarp.class);

    private static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        return instructions
                .stream()
                .anyMatch(f -> f.hashValue() == instruction.hashValue() && f.content().equals(instruction.content()));
    }

    /**
     * Method to calculate the percentage of similarity of two list of instructions
     * @param instructionsJar1 First list of instructions
     * @param instructionsJar2 Second list of instructions
     * @return The percentage that correspond to the score of the comparing of the two list
     */
    public static int onInstructions(List<Instruction> instructionsJar1, List<Instruction> instructionsJar2) {
        var nbActualInstruction = 0;
        double nbInstruction = instructionsJar1.size();
        var callables = new ArrayList<Callable<Boolean>>();
        var firstInstructions = instructionsJar1.stream().sorted(Comparator.comparing(Instruction::hashValue)).toList();
        var secondInstructions = instructionsJar2.stream().sorted(Comparator.comparing(Instruction::hashValue)).toList();
        try(var service = Executors.newFixedThreadPool(500)){
            for(var instruction: firstInstructions) {
                callables.add(() -> compareInstructionWithJarInstructions(instruction, secondInstructions));
            }
            var futures = service.invokeAll(callables);
            for(var future : futures){
                if(future.state() == Future.State.SUCCESS && future.resultNow()){
                    nbActualInstruction++;
                }
            }
            service.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("end comparing with");
        return (int) ((nbActualInstruction / nbInstruction) * 100);
    }
}