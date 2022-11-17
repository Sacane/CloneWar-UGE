package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.DtoPersistable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record ArtifactUploadDTO(String name, String url, String date, FilePart document) implements DtoPersistable<Artifact> {
    @Override
    public Artifact toEntity() {
        return new Artifact(name, url, LocalDate.now());
    }
}
