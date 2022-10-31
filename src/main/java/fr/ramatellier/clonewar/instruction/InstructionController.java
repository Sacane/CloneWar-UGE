package fr.ramatellier.clonewar.instruction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class InstructionController {

    private final InstructionService service;

    public InstructionController(InstructionService service){
        this.service = service;
    }

    @GetMapping(path = "/api/instructions/all")
    public Flux<InstructionDTO> findAll(){
        return service.findAll();
    }

}
