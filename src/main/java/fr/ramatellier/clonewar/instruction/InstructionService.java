package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.artifact.ArtifactRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Service
public class InstructionService {
    private final InstructionRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final Scheduler schedulerCtx;

    public InstructionService(InstructionRepository repository, TransactionTemplate transactionTemplate, @Qualifier("schedulerCtx") Scheduler schedulerCtx) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
        this.schedulerCtx = schedulerCtx;
    }

    public Flux<InstructionDTO> findAll() {
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx).map(i -> new InstructionDTO(i.filename(), String.valueOf(i.getLineNumberStart()), String.valueOf(i.hashValue())));
    }
}
