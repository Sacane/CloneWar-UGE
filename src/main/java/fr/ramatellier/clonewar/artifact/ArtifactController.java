package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.artifact.dto.ArtifactDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactSaveDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactUploadDTO;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;


@RestController
public class ArtifactController {

    private final ArtifactService service;
    private static final Logger LOGGER = Logger.getLogger(ArtifactController.class.getName());
    public static final Path UPLOAD_PATH = Paths.get("./src/main/resources/upload/");
    public ArtifactController(ArtifactService service){
        this.service = service;
    }

    @PostMapping(path = "/api/artifact/save")
    public Mono<ArtifactDTO> save(@RequestBody ArtifactSaveDTO artifact){
        LOGGER.info("Trying to persist an simple artifact without instructions");
        return service.saveArtifact(artifact.toEntity());
    }

    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts(){
        LOGGER.info("Starting to retrieve all artifacts in database");
        return service.findAll().delayElements(Duration.ofMillis(150)).map(Artifact::toDto);
    }

    public Mono<Void> saveArtifact(ArtifactUploadDTO dto){
        return service.saveArtifact(dto);
    }

    @PostMapping(path="/api/artifact/upload", headers = "content-type=multipart/*")
    public Mono<Void> uploadJarFile(@RequestPart("jar") Mono<FilePart> jarFile){
        LOGGER.info("Attempt to upload a file: ");
        return jarFile
                .doOnNext(fp -> LOGGER.info("Received file : " + fp.filename()))
                .flatMap(fp -> fp.transferTo(UPLOAD_PATH.resolve(fp.filename())))
                .then();
    }

    @PostMapping(path = "/api/artifact/create")
    public Mono<ArtifactSaveDTO> buildAndPersistArtifact(@RequestBody ArtifactSaveDTO dto) throws IOException {
        LOGGER.info("Attempt to create an artifact : ");
        return service.saveArtifactWithInstruction(dto);
    }
}
