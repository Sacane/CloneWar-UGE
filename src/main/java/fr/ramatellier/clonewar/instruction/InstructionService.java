package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.artifact.ArtifactRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.util.logging.Logger;

@Service
public class InstructionService {
    private final InstructionRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final Scheduler schedulerCtx;
    private final static Logger LOGGER = Logger.getLogger(InstructionService.class.getName());

    public InstructionService(InstructionRepository repository, TransactionTemplate transactionTemplate, @Qualifier("schedulerCtx") Scheduler schedulerCtx) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
        this.schedulerCtx = schedulerCtx;
    }

    public Flux<InstructionDTO> findAll() {
        LOGGER.info("Starting retrieve instructions and then put it into a Flux");
        var defer = Flux.defer(() -> Flux.fromIterable(repository.findAll()));
        return defer.subscribeOn(schedulerCtx).map(i -> new InstructionDTO(i.filename(), String.valueOf(i.getLineNumberStart()), String.valueOf(i.hashValue())));
    }
}
