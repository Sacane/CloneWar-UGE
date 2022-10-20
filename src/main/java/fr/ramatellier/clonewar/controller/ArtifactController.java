package fr.ramatellier.clonewar.controller;

import fr.ramatellier.clonewar.dto.ArtifactSaveDTO;
import fr.ramatellier.clonewar.persistence.model.Artifact;
import fr.ramatellier.clonewar.persistence.repository.ArtifactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class ArtifactController {
    @Autowired
    private ArtifactRepository service;


    @PostMapping(path = "/api/artifact/save")
    public Artifact save(@RequestBody ArtifactSaveDTO artifact){
        var entity = new Artifact(artifact.name(), artifact.url(), artifact.date());
        return service.save(entity);
    }
    @GetMapping(path= "/api/test")
    public Flux<String> send(){
        return Flux.fromIterable(List.of("String1", "String2", "String3")).delayElements(Duration.ofMillis(150));
    }
}
