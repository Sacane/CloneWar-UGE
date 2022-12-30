package fr.ramatellier.clonewar.service;

import fr.ramatellier.clonewar.exception.InvalidJarException;
import fr.ramatellier.clonewar.exception.PomNotSameException;
import fr.ramatellier.clonewar.exception.UniqueConstraintException;
import fr.ramatellier.clonewar.exception.PomNotFoundException;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import fr.ramatellier.clonewar.persistence.artifact.Artifact;
import fr.ramatellier.clonewar.persistence.artifact.ArtifactRepository;
import fr.ramatellier.clonewar.rest.artifact.ArtifactDTO;
import fr.ramatellier.clonewar.service.util.ByteResourceReader;
import fr.ramatellier.clonewar.service.util.PomExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ArtifactService {
    private final static Logger LOGGER = Logger.getLogger(ArtifactService.class.getName());
    private final ArtifactRepository repository;
    private final Scheduler schedulerCtx;

    public ArtifactService(ArtifactRepository repository, @Qualifier("schedulerCtx") Scheduler schedulerCtx) {
        this.repository = repository;
        this.schedulerCtx = schedulerCtx;
    }

    static void checkSame(byte[] srcContent, byte[] mainContent) {
        var srcReader = new ByteResourceReader(srcContent);
        var mainReader = new ByteResourceReader(mainContent);
        try{
            var srcOpt = srcReader.searchForFileContent("pom.xml");
            var mainOpt = mainReader.searchForFileContent("pom.xml");
            if(srcOpt.isEmpty() || mainOpt.isEmpty()) throw new PomNotFoundException("pom not found");
            var src = srcOpt.get();
            var main = mainOpt.get();
            if(!src.equals(main)) throw new PomNotSameException("The main and source jar are not the same.");
        }catch (IOException e){
            throw new InvalidJarException("Could not open the jar", e.getCause());
        }
    }

    private Artifact createArtifactByInfos(String mainName, String srcName, byte[] srcContent, byte[] mainContent) throws IOException {
        LOGGER.info("Create artifact");
        checkSame(srcContent, mainContent); //Check if the pom.xml are equals or not
        var artifactId = PomExtractor.retrieveAttribute(srcContent, PomExtractor.XMLObject.ARTIFACT_ID)
                .orElseThrow(() -> new PomNotFoundException("There is no pom xml or it doesn't contains any artifactId for the project"));
        LOGGER.info("ArtifactId retrieved successfully");
        var version = PomExtractor.retrieveAttribute(srcContent, PomExtractor.XMLObject.VERSION)
                        .orElse("Unknown");
        LOGGER.info("Attribute artifactId and version are retrieved successfully");
        var instructions = InstructionBuilder.buildInstructionFromJar(artifactId, mainContent);
        var artifact = new Artifact(artifactId, mainName, srcName, LocalDate.now(), mainContent, srcContent, version);
        artifact.addAllInstructions(instructions);
        System.out.println("Instructions --> " + instructions);
        return artifact;
    }

    private Artifact saveArtifact(Artifact artifact) {
        try{
            return repository.save(artifact);
        }catch (DataAccessException e){
            throw new UniqueConstraintException(e);
        }
    }

    /**
     * Method that will create an artifact to upload it to the database
     * @param mainFile The file that contains the .class files
     * @param srcFile The file that contains the .java files
     * @return A Mono of the ArtifactDTO that we upload
     */
    public Mono<ArtifactDTO> createArtifactFromFileAndThenPersist(File mainFile, File srcFile) {
        return Mono.fromCallable(() -> {
            var artifact = createArtifactByInfos(mainFile.getName(), srcFile.getName(), Files.readAllBytes(Path.of(mainFile.getAbsolutePath())), Files.readAllBytes(Path.of(srcFile.getAbsolutePath())));
            if(!mainFile.delete() || srcFile.delete()){
                LOGGER.severe("An error occurs because a file can't be deleted");
            }
            return saveArtifact(artifact).toDto();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     * Method that will find all the artifacts of the database
     * @return A Flux of all the ArtifactDTO that already have been upload to the database
     */
    public Flux<ArtifactDTO> findAll() {
        return Flux.defer(() -> Flux.fromIterable(repository.findAllArtifact()
                .stream()
                .map(Artifact::toDto)
                .toList())).subscribeOn(schedulerCtx);
    }

    /**
     * Method that will find the name of the artifact that have the specified id
     * @param id The id of the artifact we want to find in the database
     * @return A Mono with the name of the artifact that we search
     */
    public Mono<String> getNameById(String id) {
        return Mono.fromCallable(() -> repository.nameById(UUID.fromString(id))).subscribeOn(Schedulers.boundedElastic());
    }
}
