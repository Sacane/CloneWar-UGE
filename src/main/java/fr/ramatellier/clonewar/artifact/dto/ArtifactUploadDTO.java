package fr.ramatellier.clonewar.artifact.dto;

import fr.ramatellier.clonewar.DtoPersistable;
import fr.ramatellier.clonewar.artifact.Artifact;
import org.springframework.http.codec.multipart.FilePart;
import java.time.LocalDate;

public record ArtifactUploadDTO(String name, String url, String date, FilePart document) implements DtoPersistable<Artifact> {
    @Override
    public Artifact toEntity() {
        return new Artifact(name, url, null, LocalDate.now(), null, null);
    }
}
