package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.artifact.dto.ScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class KaplinController {
    @Autowired
    private KaplinService service;

    @GetMapping(path="/api/kaplin/score/{id}")
    public Flux<ScoreDTO> scoreForJar(@PathVariable("id") String id) throws IOException {
        ArrayList<ScoreDTO> list = new ArrayList<>();
        List<String> ids = service.findAllIdExceptId(id);
        var instructions = service.findInstructionsForId(id);

        for(var secondId: ids) {
            System.out.println(service.findNameForId(secondId));
            var secondInstructions = service.findInstructionsForId(secondId);
            var score = Kaplin.compareJarInstructions(instructions, secondInstructions);
            list.add(new ScoreDTO(secondId, service.findNameForId(secondId), String.valueOf(score) + "%"));
        }

        return Flux.fromIterable(list);
    }
}