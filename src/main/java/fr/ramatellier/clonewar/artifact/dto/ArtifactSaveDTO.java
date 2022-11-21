package fr.ramatellier.clonewar.artifact.dto;

import fr.ramatellier.clonewar.DtoPersistable;
import fr.ramatellier.clonewar.artifact.Artifact;

import java.time.LocalDate;

public record ArtifactSaveDTO(String name, String url, String date, byte[] jarfile) implements DtoPersistable<Artifact> {
    @Override
    public Artifact toEntity() {
        return new Artifact(name, url, LocalDate.parse(date), jarfile);
    }
}
