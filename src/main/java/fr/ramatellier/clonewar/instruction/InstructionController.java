package fr.ramatellier.clonewar.instruction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@RestController
public class InstructionController {

    private final InstructionService service;
    private static final Logger LOGGER = Logger.getLogger(InstructionController.class.getName());

    public InstructionController(InstructionService service){
        this.service = service;
    }

    @GetMapping(path = "/api/instructions/all")
    public Flux<InstructionDTO> findAll(){
        LOGGER.info("Search for all Instructions");
        return service.findAll();
    }

}
