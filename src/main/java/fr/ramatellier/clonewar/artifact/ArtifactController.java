package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.CloneWarApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;


@RestController
public class ArtifactController {
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
