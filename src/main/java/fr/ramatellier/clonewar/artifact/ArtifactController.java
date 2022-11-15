package fr.ramatellier.clonewar.artifact;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;


@RestController
public class ArtifactController {
    private static final Path UPLOAD_PATH = Paths.get("./src/main/resources/upload/");
    private final ArtifactService service;
    private static final Logger LOGGER = Logger.getLogger(ArtifactController.class.getName());
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



    @PostMapping(path="/api/artifact/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<Void> uploadJarFile(@ModelAttribute Mono<ArtifactUploadDTO> dtoMono){
        LOGGER.info("Attempt to upload a file: ");
        return dtoMono
                .doOnNext(fp -> {
                    LOGGER.info("Received file : " + fp.url());
                    LOGGER.info("Received artifact name : " + fp.name());
                    LOGGER.info("Received artifact url : " + fp.url());
                })
                .flatMap(fp -> fp.document().transferTo(UPLOAD_PATH.resolve(fp.url())))
                .then();
    }

    @PostMapping(path = "/api/artifact/persist")
    public Mono<ArtifactDTO> putArtifacts(@RequestBody ArtifactSaveDTO dto){
        LOGGER.info("Persist an artifact and its instructions");
        try {
            return service.saveArtifactWithInstruction(dto);
        } catch (IOException e) {
            LOGGER.severe("Cannot found jar : " + dto.url());
            return Mono.error(new IOException("No file as " + dto.url() + " found"));
        }
    }
}
