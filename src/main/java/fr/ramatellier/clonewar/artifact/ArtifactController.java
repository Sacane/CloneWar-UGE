package fr.ramatellier.clonewar.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;


@RestController
public class ArtifactController {
    private final ArtifactService service;

    public ArtifactController(ArtifactService service){
        this.service = service;
    }

    @PostMapping(path = "/api/artifact/save")
    public Mono<ArtifactDTO> save(@RequestBody ArtifactSaveDTO artifact){
        return service.saveArtifact(artifact.toEntity());
    }

    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts(){
        return service.findAll().delayElements(Duration.ofMillis(150)).map(Artifact::toDto);
    }

    @PostMapping(path = "/api/artifact/persist")
    public Mono<ArtifactDTO> putArtifacts(@RequestBody ArtifactSaveDTO dto){
        try {
            return service.saveArtifactWithInstruction(dto);
        } catch (IOException e) {
            return null;
        }
    }
}
