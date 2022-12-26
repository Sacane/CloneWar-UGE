package fr.ramatellier.clonewar.rabin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RabinKarpController {
    @Autowired
    private RabinKapService service;

    /**
     * Method of the API to get the score of the artifact that got the specified id with all the others artifacts
     * @param id The id of the artifact we want to compare with the others
     * @return A flux of ScoreDTO that contains the id, the name and the score of the artifacts we have compare with the specified artifact
     */
    @GetMapping(path="/api/kaplin/score/{id}")
    public Flux<ScoreDTO> scoreForJar(@PathVariable("id") String id) {
        return Flux.fromIterable(service.scoreByJar(id));
    }
}