package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.util.AsmParser;
import fr.ramatellier.clonewar.util.JarReader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
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
        return service.findAll().doOnNext(p -> LOGGER.info("End retrieving artifacts"));
    }

    @PostMapping(path="/api/artifact/upload", headers = "content-type=multipart/*")
    public Mono<ArtifactDTO> uploadJarFile(@RequestPart("src") FilePart srcFile, @RequestPart("main") FilePart mainFile) throws IOException {
        LOGGER.info("Trying to analyze main file : " + mainFile.filename() + " and src file : " + srcFile.filename());
        var file1 = new File(srcFile.filename());
        var file2 = new File(mainFile.filename());
        file1.createNewFile();
        file2.createNewFile();
        return srcFile.transferTo(file1).then(mainFile.transferTo(file2)).then(service.createArtifactFromFileAndThenPersist(file1, file2));
    }

    @GetMapping(path="/api/artifact/name/{id}")
    public Mono<String> getNameById(@PathVariable("id") String id) {
        LOGGER.info("trying to get name from id");
        return service.getNameById(id);
    }
}
