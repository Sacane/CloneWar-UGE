package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.artifact.dto.ScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
public class KaplinController {
    @Autowired
    private KaplinService service;

    @GetMapping(path="/api/kaplin/score/{id}")
    public Flux<ScoreDTO> scoreForJar(@PathVariable("id") String id) {
        ArrayList<ScoreDTO> list = new ArrayList<>();
        List<String> ids = service.findAllIdExceptId(id);

        System.out.println(id);
        System.out.println(ids);

        for(var secondId: ids) {
            System.out.println(service.findNameForId(secondId));
            list.add(new ScoreDTO(secondId, service.findNameForId(secondId), "0"));
        }

        return Flux.fromIterable(list);
    }
}