package fr.ramatellier.clonewar.rabin;

import fr.ramatellier.clonewar.artifact.dto.ScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@RestController
public class RabinKarpController {
    @Autowired
    private RabinKapService service;

    @GetMapping(path="/api/kaplin/score/{id}")
    public Flux<ScoreDTO> scoreForJar(@PathVariable("id") String id) {
        return Flux.fromIterable(service.scoreByJar(id));
    }
}