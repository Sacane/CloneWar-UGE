package fr.ramatellier.clonewar.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class ArtifactController {
    @Autowired
    private ArtifactService service;
    @PostMapping(path = "/api/artifact/save")
    public Mono<ArtifactDTO> save(@RequestBody ArtifactSaveDTO artifact){
        var entity = new Artifact(artifact.name(), artifact.url(), artifact.date());
        return service.saveArtifact(entity);
    }

    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts(){
        return service.findAll().delayElements(Duration.ofMillis(150)).map(m -> new ArtifactDTO(m.name(), m.url(), m.inputDate().toString()));
    }
    @GetMapping(path= "/api/test")
    public Flux<String> send(){
        return Flux.fromIterable(List.of("String1", "String2", "String3")).delayElements(Duration.ofMillis(150));
    }
}
