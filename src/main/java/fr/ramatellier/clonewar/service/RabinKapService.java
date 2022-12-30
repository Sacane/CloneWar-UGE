package fr.ramatellier.clonewar.service;

import fr.ramatellier.clonewar.persistence.Instruction.Instruction;
import fr.ramatellier.clonewar.persistence.artifact.ArtifactRepository;
import fr.ramatellier.clonewar.rest.rabin.ScoreDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class RabinKapService {
    private final ArtifactRepository artifactRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabinKapService.class);

    public RabinKapService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    /**
     * Method that will find the name of all the artifacts except the one who get the specified id
     * @param id The id of the artifact that we don't want to get his name
     * @return The list of the name of all the artifacts we find
     */
    public List<String> findAllIdExceptId(String id) {
        ArrayList<String> names = new ArrayList<>();

        artifactRepository.findAll().forEach(a -> {
             if(!a.id().toString().equals(id))
                names.add(a.id().toString());
        });

        return names;
    }

    /**
     * Method that will get the score of the artifact with the specified id with all the others artifacts
     * @param id The id of the artifact we want to compare to the others
     * @return The list of ScoreDTO of each artifact with their name, id and score compare to the artifact who get the specified id
     */
    public List<ScoreDTO> scoreByJar(String id) {
        var list = new ArrayList<ScoreDTO>();
        var ids = findAllIdExceptId(id);
        var instructions = artifactRepository.customFindById(UUID.fromString(id)).instructions();

        for(var secondId: ids) {
            var second = artifactRepository.customFindById(UUID.fromString(secondId));
            var secondInstructions = second.instructions();
            LOGGER.info("get the artifact -> " + second.name());
            var score = onInstructions(instructions, secondInstructions);
            LOGGER.info("compute score finished successfully");
            list.add(new ScoreDTO(secondId, second.name(), String.valueOf(score)));
        }
        LOGGER.info("score is all done");
        return list.stream().sorted(Comparator.comparing(ScoreDTO::score)).toList();
    }
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
    static int onInstructions(List<Instruction> instructionsJar1, List<Instruction> instructionsJar2) {
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