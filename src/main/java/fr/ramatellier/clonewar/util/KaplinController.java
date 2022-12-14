package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.artifact.ArtifactController;
import fr.ramatellier.clonewar.artifact.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class KaplinController {
    @Autowired
    private KaplinService service;

    @GetMapping(path="/api/kaplin/score/{id}")
    public Flux<Integer> scoreForJar(@PathVariable("id") String id) {
        String name = service.findNameWithId(id);
        List<String> names = service.findAllNameExceptId(id);
        var instructions = service.findInstructionsForId(id);

        System.out.println(name);
        System.out.println(names);

        return Flux.just(0);
    }
}