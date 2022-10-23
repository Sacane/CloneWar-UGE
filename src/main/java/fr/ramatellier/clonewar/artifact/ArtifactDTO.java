package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.DtoPersistable;

import java.time.LocalDate;
public record ArtifactDTO(String id, String name, String date, String url) implements DtoPersistable<Artifact> {

    @Override
    public Artifact toEntity() {
        return new Artifact(name, url, LocalDate.parse(date));
    }
}
