package fr.ramatellier.clonewar.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.UUID;

@Service
public class ArtifactService {

    private final ArtifactRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final Scheduler schedulerCtx;

    public ArtifactService(ArtifactRepository repository, TransactionTemplate transactionTemplate, @Qualifier("schedulerCtx") Scheduler schedulerCtx){
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
        this.schedulerCtx = schedulerCtx;
    }

    public Mono<ArtifactDTO> saveArtifact(Artifact entity){
        var dto = new ArtifactDTO(entity.id().toString(), entity.name(), entity.inputDate().toString(), entity.url());
        return Mono.fromCallable(() -> transactionTemplate.execute(status -> {
            repository.save(entity);
            return dto;
        })).subscribeOn(schedulerCtx);
    }

    public Flux<Artifact> findAll(){
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx);
    }
}
