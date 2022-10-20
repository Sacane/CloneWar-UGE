package fr.ramatellier.clonewar.artifact;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ArtifactRepository extends CrudRepository<Artifact, UUID> {}
