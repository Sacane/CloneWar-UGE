package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.instruction.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class RabinKarp {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabinKarp.class);
    private static boolean compareInstructionWithJarInstructions(Instruction instruction, List<Instruction> instructions) {
        for(var elem: instructions) {
            if(elem.hashValue() == instruction.hashValue() && elem.content().equals(instruction.content())) {
                return true;
            }
        }
        return false;
//        LOGGER.info("start comparing");
//        var value = instructions.stream().anyMatch(f -> f.hashValue() == instruction.hashValue());
//        LOGGER.info("end comparing");
//        return value;
    }

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

        /*
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
*/

        LOGGER.info("end comparing with");
        return (int) ((nbActualInstruction / nbInstruction) * 100);
    }
}