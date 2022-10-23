package fr.ramatellier.clonewar.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


@RestController
public class ArtifactController {

    private final ArtifactService service;

    public ArtifactController(ArtifactService service){
        this.service = service;
    }

    @PostMapping(path = "/api/artifact/save")
    public Mono<ArtifactDTO> save(@RequestBody ArtifactDTO artifact){
        return service.saveArtifact(artifact.toEntity());
    }

    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts(){
        return service.findAll().delayElements(Duration.ofMillis(150)).map(Artifact::toDto);
    }
}
