package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.artifact.ArtifactRepository;
import fr.ramatellier.clonewar.instruction.Instruction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KaplinService {
    private final ArtifactRepository artifactRepository;

    public KaplinService(ArtifactRepository artifactRepository) {
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

    public List<Instruction> findInstructionsForId(String id) {
        return artifactRepository.findById(UUID.fromString(id)).get().instructions();
    }
}