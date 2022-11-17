package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.artifact.dto.ArtifactDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactSaveDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactUploadDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;


@RestController
public class ArtifactController {

    private final ArtifactService service;
    private static final Logger LOGGER = Logger.getLogger(ArtifactController.class.getName());
    private static final Path UPLOAD_PATH = Paths.get("./src/main/resources/upload/");
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


    private Mono<Void> uploadFile(Mono<ArtifactUploadDTO> monoDto){
        return monoDto.flatMap(fp -> fp.document().transferTo(UPLOAD_PATH.resolve(fp.url())));
    }
    @PostMapping(path="/api/artifact/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<Void> uploadJarFile(@ModelAttribute ArtifactUploadDTO dtoMono){
        LOGGER.info("Attempt to upload a file: ");
        return uploadFile(Mono.just(dtoMono));
    }

//    @PostMapping(path = "/api/artifact/persist")
//    public Mono<ArtifactDTO> putArtifacts(@RequestBody ArtifactSaveDTO dto){
//        LOGGER.info("Persist an artifact and its instructions");
//        try {
//            return service.saveArtifactWithInstruction(dto);
//        } catch (IOException e) {
//            LOGGER.severe("Cannot found jar : " + dto.url());
//            return Mono.error(new IOException("No file as " + dto.url() + " found"));
//        }
//    }
}
