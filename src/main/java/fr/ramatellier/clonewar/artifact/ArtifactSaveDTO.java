package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.DtoPersistable;

import java.time.LocalDate;

public record ArtifactSaveDTO(String name, String url, String date) implements DtoPersistable {
    @Override
    public Artifact toEntity() {
        return new Artifact(name, url, LocalDate.parse(date));
    }
}
