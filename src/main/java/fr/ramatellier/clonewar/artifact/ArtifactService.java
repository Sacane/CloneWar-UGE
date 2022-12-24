package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.exception.InvalidJarException;
import fr.ramatellier.clonewar.exception.UniqueConstraintException;
import fr.ramatellier.clonewar.exception.PomNotFoundException;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import fr.ramatellier.clonewar.util.PomExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
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
    private final TransactionTemplate transactionTemplate;

    public ArtifactService(ArtifactRepository repository, @Qualifier("schedulerCtx") Scheduler schedulerCtx, TransactionTemplate transactionTemplate){
        this.repository = repository;
        this.schedulerCtx = schedulerCtx;
        this.transactionTemplate = transactionTemplate;
    }

    private Artifact createArtifactByInfos(String mainName, String srcName, byte[] srcContent, byte[] mainContent) throws IOException {
        LOGGER.info("Create artifact");
        var artifactId = PomExtractor.retrieveAttribute(srcContent, PomExtractor.XMLObject.ARTIFACT_ID)
                .orElseThrow(() -> new PomNotFoundException("There is no pom xml or it doesn't contains any artifactId for the project"));
        var version = PomExtractor.retrieveAttribute(srcContent, PomExtractor.XMLObject.VERSION)
                .orElseThrow(() -> new PomNotFoundException("here is no pom xml or it doesn't contains any version for the project"));
        var instructions = InstructionBuilder.buildInstructionFromJar(artifactId, mainContent);
        var artifact = new Artifact(artifactId, mainName, srcName, LocalDate.now(), mainContent, srcContent, version);
        artifact.addAllInstructions(instructions);
        System.out.println("Instructions --> " + instructions);
        return artifact;
    }

    private Mono<byte[]> retrieveBytesFromFilePart(FilePart filePart){
        return filePart.content().publishOn(Schedulers.boundedElastic()).map(part -> {
            try {
                return part.asInputStream().readAllBytes();
            } catch (IOException e) {
                throw new InvalidJarException(e);
            }
        }).single();
    }

    private Artifact saveArtifact(Artifact artifact){
        try{
            return repository.save(artifact);
        }catch (DataAccessException e){
            throw new UniqueConstraintException(e);
        }
    }

    public Mono<ArtifactDTO> createArtifactFromFileAndThenPersist(FilePart mainPart, FilePart srcPart){
        LOGGER.info("Persist files main :" + mainPart.filename() + " and its src : " + srcPart.filename());
        return retrieveBytesFromFilePart(mainPart)
                .flatMap(mainBytes -> retrieveBytesFromFilePart(srcPart).map(srcBytes -> {
                                    try {
                                        LOGGER.info("createArtifactFromFile");
                                        var artifact = createArtifactByInfos(mainPart.filename(), srcPart.filename(), srcBytes, mainBytes);
                                        LOGGER.info("Artifact created");
                                        return saveArtifact(artifact).toDto();
                                    } catch (IOException e) {
                                        LOGGER.info("error");
                                        throw new InvalidJarException(e);
                                    }
                                }
                ));
    }
    public Mono<ArtifactDTO> createArtifactFromFileAndThenPersist(byte[] byteMain, byte[] byteSrc){
        return Mono.fromCallable(() -> {
           var artifact = createArtifactByInfos("main", "src", byteMain, byteSrc);
           return saveArtifact(artifact).toDto();
        });
    }
    public Mono<ArtifactDTO> createArtifactFromFileAndThenPersist(File mainFile, File srcFile){
        return Mono.fromCallable(() -> {
            var artifact = createArtifactByInfos(mainFile.getName(), srcFile.getName(), Files.readAllBytes(Path.of(mainFile.getAbsolutePath())), Files.readAllBytes(Path.of(srcFile.getAbsolutePath())));
            if(!mainFile.delete() || srcFile.delete()){
                LOGGER.severe("An error occurs because a file can't be deleted");
            }
            return saveArtifact(artifact).toDto();
        }).publishOn(Schedulers.boundedElastic());
    }

    public Flux<ArtifactDTO> findAll(){
        return Flux.defer(() -> Flux.fromIterable(repository.findAllArtifact()
                .stream()
                .map(Artifact::toDto)
                .toList())).subscribeOn(schedulerCtx);
    }

    public Mono<String> getNameById(String id) {
        return Mono.fromCallable(() -> repository.nameById(UUID.fromString(id))).subscribeOn(Schedulers.boundedElastic());
    }
}
