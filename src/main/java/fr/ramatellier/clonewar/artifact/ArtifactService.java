package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.artifact.dto.ArtifactDTO;
import fr.ramatellier.clonewar.artifact.dto.ArtifactSaveDTO;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
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
            return new ArtifactDTO(entityResponse.id().toString(), entityResponse.name(), entityResponse.inputDate().toString(), entityResponse.url(), null);
        })).subscribeOn(schedulerCtx);
    }

    //TODO remove at end
    public Mono<ArtifactDTO> createArtifactFromFileAndThenPersist(FilePart filePart){
        LOGGER.info("Start parsing a file");
        LOGGER.info("On schedule file :" + filePart.filename());
        return filePart.content().publishOn(Schedulers.boundedElastic()).map(signal -> {
            try(var sig = signal.asInputStream()){
                var bytes = sig.readAllBytes();
                var artifact = new Artifact(filePart.filename(), filePart.filename(), LocalDate.now(), bytes);
                return repository.save(artifact).toDto();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).single();
    }

    public Flux<Artifact> findAll(){
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx);
    }
}
