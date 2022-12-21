package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.artifact.ArtifactRepository;
import fr.ramatellier.clonewar.instruction.Instruction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RabinKapService {
    private final ArtifactRepository artifactRepository;

    public RabinKapService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public List<String> findAllIdExceptId(String id) {
        ArrayList<String> names = new ArrayList<>();

        artifactRepository.findAll().forEach(a -> {
            // if(!a.id().toString().equals(id))
                names.add(a.id().toString());
        });

        return names;
    }

    public String findNameForId(String id) {
        return artifactRepository.findById(UUID.fromString(id)).get().name();
    }

    public List<Instruction> findInstructionsForId(String id) {
        return artifactRepository.findById(UUID.fromString(id)).get().instructions();
    }

    public ArrayList<ScoreDTO> scoreByJar(String id){
        var list = new ArrayList<ScoreDTO>();
        var ids = findAllIdExceptId(id);
        var instructions = findInstructionsForId(id);

        for(var secondId: ids) {
            var secondInstructions = findInstructionsForId(secondId);
            var score = RabinKarp.compareJarInstructions(instructions, secondInstructions);
            list.add(new ScoreDTO(secondId, findNameForId(secondId), score + "%"));
        }
        return list;
    }


}