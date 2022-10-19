package fr.ramatellier.clonewar.persistence.service;

import fr.ramatellier.clonewar.persistence.model.Artifact;
import fr.ramatellier.clonewar.persistence.repository.ArtifactRepository;
import org.springframework.stereotype.Service;


@Service
public class ArtifactService {

    private final ArtifactRepository repository;

    public ArtifactService(ArtifactRepository repository){
        this.repository = repository;
    }
    public Artifact saveArtifact(Artifact entity){
        return repository.save(entity);
    }
}
