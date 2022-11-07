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
@CrossOrigin(origins = "*", allowedHeaders = "*")
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



    @PostMapping(path="/api/artifact/upload", headers = "content-type=multipart/*")
    public Mono<Void> uploadJarFile(@RequestPart("fileJar") Mono<FilePart> jarFile){
        LOGGER.info("Attempt to upload a file: ");
        var res = jarFile
                .doOnNext(fp -> LOGGER.info("Received file : " + fp.filename()))
                .flatMap(fp -> fp.transferTo(UPLOAD_PATH.resolve(fp.filename())))
                .then();
        return res;
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
