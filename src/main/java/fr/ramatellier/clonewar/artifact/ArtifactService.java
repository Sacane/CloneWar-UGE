package fr.ramatellier.clonewar.artifact;

import com.mchange.util.AssertException;
import fr.ramatellier.clonewar.artifact.dto.ArtifactDTO;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import fr.ramatellier.clonewar.util.AsmParser;
import fr.ramatellier.clonewar.util.ByteResourceReader;
import fr.ramatellier.clonewar.util.PomExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ArtifactService {

    private final static Logger LOGGER = Logger.getLogger(ArtifactService.class.getName());
    private final ArtifactRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final Scheduler schedulerCtx;
    private static final String UPLOAD_PATH = "./src/main/resources/upload/";

    public ArtifactService(ArtifactRepository repository, TransactionTemplate transactionTemplate, @Qualifier("schedulerCtx") Scheduler schedulerCtx){
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
        this.schedulerCtx = schedulerCtx;
    }

    /**
     * TODO REMOVE
     * @Deprecated
     * @param entity
     * @return
     */
    public Mono<ArtifactDTO> saveArtifact(Artifact entity){
        return Mono.fromCallable(() -> transactionTemplate.execute(status -> {
            var entityResponse = repository.save(entity);
            return new ArtifactDTO(entityResponse.id().toString(), entityResponse.name(), entityResponse.inputDate().toString(), entityResponse.url(), null);
        })).subscribeOn(schedulerCtx);
    }

    //SRC -> .JAVA
    ///MAIN -> .CLASS
    private Artifact createArtifactByInfos(byte[] srcContent, byte[] mainContent) throws IOException {
        var artifactIdOptional = PomExtractor.getProjectArtifactId(srcContent);
        if(artifactIdOptional.isEmpty()) throw new NoSuchElementException("There is no artifactId in this pom content");
        var artifactId = artifactIdOptional.get();
        var instructions = InstructionBuilder.buildInstructionFromJar(artifactId, mainContent);
        var artifact = new Artifact(artifactId, artifactId, LocalDate.now(), mainContent, srcContent); //TODO replace second argument to URL
        artifact.addAllInstructions(instructions);
        System.out.println("Instructions --> " + instructions);
        return artifact;
    }

    public Mono<ArtifactDTO> createArtifactFromFileAndThenPersist(FilePart filePart){
        LOGGER.info("Persist file :" + filePart.filename());
        return filePart.content().publishOn(Schedulers.boundedElastic()).map(signal -> {
            try(var sig = signal.asInputStream()){
                var bytes = sig.readAllBytes();
                var srcBytes = Files.readAllBytes(Path.of("src/test/resources/samples/SeqSrc.jar")); //TODO remove this and replace by the given src jar(from front)
                var artifact = createArtifactByInfos(srcBytes, bytes);
                return repository.save(artifact).toDto();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }).single();
    }

    public Flux<Artifact> findAll(){
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx);
    }

    public Mono<String> getNameById(String id) {
        return Mono.just(repository.findById(UUID.fromString(id)).get().name());
    }

    public void configure(){
        var artifact = repository.findByName("");
    }
}
