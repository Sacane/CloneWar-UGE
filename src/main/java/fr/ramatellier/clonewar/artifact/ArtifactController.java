package fr.ramatellier.clonewar.artifact;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.logging.Logger;


@RestController
public class ArtifactController {
    private final ArtifactService service;
    private static final Logger LOGGER = Logger.getLogger(ArtifactController.class.getName());
    public ArtifactController(ArtifactService service) {
        this.service = service;
    }

    /**
     * Method of the API to get all the artifacts in database
     * @return A flux of ArtifactDTO
     */
    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts() {
        LOGGER.info("Start retrieving all artifacts from database");
        return service.findAll().doOnNext(p -> LOGGER.info("Artifact " + p.name() + " has been retrieve successfully"));
    }

    /**
     * Method of the API to upload a new Artifact to the database
     * @param srcFile FilePart that contains the .java files
     * @param mainFile FilePart that contains the .class files
     * @return A Mono with the ArtifactDTO that we want to upload
     */
    @PostMapping(path="/api/artifact/upload", headers = "content-type=multipart/*")
    public Mono<ArtifactDTO> uploadJarFile(@RequestPart("src") FilePart srcFile, @RequestPart("main") FilePart mainFile) {
        LOGGER.info("Start indexing from jar : " + mainFile.filename() + " and its sources : " + srcFile.filename());
        var file1 = new File(srcFile.filename());
        var file2 = new File(mainFile.filename());
        return srcFile
                .transferTo(file1)
                .then(mainFile.transferTo(file2))
                .then(service.createArtifactFromFileAndThenPersist(file1, file2));
    }

    /**
     * Methode of the API that will get an artifact name with his id
     * @param id It's the id of the artifact that we search
     * @return The name of the artifact with the specified id
     */
    @GetMapping(path="/api/artifact/name/{id}")
    public Mono<String> getNameById(@PathVariable("id") String id) {
        LOGGER.info("trying to get name from id");
        return service.getNameById(id);
    }
}
