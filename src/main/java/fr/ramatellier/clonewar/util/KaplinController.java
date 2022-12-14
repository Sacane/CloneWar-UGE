package fr.ramatellier.clonewar.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class KaplinController {
    @Autowired
    private KaplinService service;

    @GetMapping(path="/api/kaplin/score/{id}")
    public Flux<Integer> scoreForJar(@PathVariable("id") String id) {
        List<String> ids = service.findAllIdExceptId(id);
        var instructions = service.findInstructionsForId(id);

        System.out.println(id);
        System.out.println(ids);
        for(var element: instructions) {
            System.out.println(element);
        }

        return Flux.just(0);
    }
}