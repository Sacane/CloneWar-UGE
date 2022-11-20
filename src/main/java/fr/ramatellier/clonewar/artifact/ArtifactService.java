package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.artifact.dto.ArtifactDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactSaveDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactUploadDTO;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
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
            return new ArtifactDTO(entityResponse.id().toString(), entityResponse.name(), entityResponse.inputDate().toString(), entityResponse.url());
        })).subscribeOn(schedulerCtx);
    }
    public Mono<Void> saveArtifact(ArtifactUploadDTO dto){
        var file = new File(dto.name());
        return dto.document().transferTo(file)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(p -> {
                    try {
                        var entity = new Artifact(dto.name(), dto.url(), LocalDate.parse(dto.date()),
                                Files.readAllBytes(file.toPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * TODO Replace
     * @param dto
     * @return
     * @throws IOException
     */
    public Mono<ArtifactSaveDTO> saveArtifactWithInstruction(ArtifactSaveDTO dto) throws IOException {
        LOGGER.info("Parsing artifacts and its instructions");
        var artifact = new Artifact(dto.name(), dto.url(), LocalDate.now());
        var list = InstructionBuilder.buildInstructionFromJar(ArtifactController.UPLOAD_PATH.resolve(dto.url()).toString());
        return Mono.fromCallable(() -> transactionTemplate.execute(status -> {
            artifact.addAllInstructions(list);
            var entityResponse = repository.save(artifact);
            LOGGER.info("Saving artifact done.");
            return new ArtifactSaveDTO(entityResponse.name(), entityResponse.inputDate().toString(), entityResponse.url());
        })).subscribeOn(schedulerCtx);
    }

    public Flux<Artifact> findAll(){
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx);
    }
}
