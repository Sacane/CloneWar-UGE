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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ArtifactService {

    private final static Logger LOGGER = Logger.getLogger(ArtifactService.class.getName());
    private final ArtifactRepository repository;
    private final Scheduler schedulerCtx;

    public ArtifactService(ArtifactRepository repository, @Qualifier("schedulerCtx") Scheduler schedulerCtx){
        this.repository = repository;
        this.schedulerCtx = schedulerCtx;
    }

    //SRC -> .JAVA
    ///MAIN -> .CLASS
    private Artifact createArtifactByInfos(String mainName, String srcName, byte[] srcContent, byte[] mainContent) throws IOException {
        var artifactId = PomExtractor.getProjectArtifactId(srcContent)
                .orElseThrow(() -> new PomNotFoundException("There is no pom.xml in this source jar"));
        var instructions = InstructionBuilder.buildInstructionFromJar(artifactId, mainContent);
        var artifact = new Artifact(artifactId, mainName, srcName, LocalDate.now(), mainContent, srcContent);
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
                                        var artifact = createArtifactByInfos(mainPart.filename(), srcPart.filename(), srcBytes, mainBytes);
                                        return saveArtifact(artifact).toDto();
                                    } catch (IOException e) {
                                        throw new InvalidJarException(e);
                                    }
                                }
                ));
    }

    public Flux<Artifact> findAll(){
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx);
    }

    public Mono<String> getNameById(String id) {
        return Mono.just(repository.findById(UUID.fromString(id)).get().name());
    }
}
