package fr.ramatellier.clonewar.artifact;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;


@RestController
public class ArtifactController {

    private final ArtifactService service;
    private static final Logger LOGGER = Logger.getLogger(ArtifactController.class.getName());
    public ArtifactController(ArtifactService service){
        this.service = service;
    }


    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts(){
        LOGGER.info("Starting to retrieve all artifacts in database");
        return service.findAll().map(Artifact::toDto);
    }

    @PostMapping(path="/api/artifact/upload", headers = "content-type=multipart/*")
    public Mono<ArtifactDTO> uploadJarFile(@RequestPart("src") FilePart srcFile, @RequestPart("main") FilePart mainFile) {
        LOGGER.info("Trying to analyze main file : " + mainFile.filename() + " and src file : " + srcFile.filename());
        return service.createArtifactFromFileAndThenPersist(mainFile, srcFile);
    }

    @GetMapping(path="/api/artifact/name/{id}")
    public Mono<String> getNameById(@PathVariable("id") String id) {
        LOGGER.info("trying to get name from id");
        return service.getNameById(id);
    }
}
