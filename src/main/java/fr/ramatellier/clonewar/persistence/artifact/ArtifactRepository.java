package fr.ramatellier.clonewar.persistence.artifact;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtifactRepository extends CrudRepository<Artifact, UUID> {
    @Query(value = "SELECT a FROM Artifact a WHERE a.name=?1")
    Artifact findByName(String name);

    @Query(value = "SELECT a.name FROM Artifact a WHERE a.id=?1")
    String nameById(UUID id);

    @Query(value = "SELECT a FROM Artifact a WHERE a.id=?1")
    Artifact customFindById(UUID id);

    @Query(value = "SELECT * FROM Artifact a", nativeQuery = true)
    List<Artifact> findAllArtifact();
}