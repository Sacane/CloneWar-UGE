package fr.ramatellier.clonewar.artifact;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

public record ArtifactUploadDTO(String name, String url, String date, FilePart document) {
}
