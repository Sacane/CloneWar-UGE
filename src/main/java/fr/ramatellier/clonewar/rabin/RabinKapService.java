package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.artifact.ArtifactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class RabinKapService {
    private final ArtifactRepository artifactRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabinKapService.class);

    public RabinKapService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public List<String> findAllIdExceptId(String id) {
        ArrayList<String> names = new ArrayList<>();

        artifactRepository.findAll().forEach(a -> {
             if(!a.id().toString().equals(id))
                names.add(a.id().toString());
        });

        return names;
    }

    public List<ScoreDTO> scoreByJar(String id){
        var list = new ArrayList<ScoreDTO>();
        var ids = findAllIdExceptId(id);
        var instructions = artifactRepository.customFindById(UUID.fromString(id)).instructions();

        for(var secondId: ids) {

            var second = artifactRepository.customFindById(UUID.fromString(secondId));
            var secondInstructions = second.instructions();
            LOGGER.info("get the artifact -> " + second.name());
            var score = RabinKarp.onInstructions(instructions, secondInstructions);
            LOGGER.info("compute score finished successfully");
            list.add(new ScoreDTO(secondId, second.name(), String.valueOf(score)));
        }
        LOGGER.info("score is all done");
        return list.stream().sorted(Comparator.comparing(ScoreDTO::score)).toList();
    }
}