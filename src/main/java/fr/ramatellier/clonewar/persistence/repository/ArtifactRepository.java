package fr.ramatellier.clonewar.persistence.repository;

import fr.ramatellier.clonewar.persistence.model.Artifact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ArtifactRepository extends CrudRepository<Artifact, UUID> {}
