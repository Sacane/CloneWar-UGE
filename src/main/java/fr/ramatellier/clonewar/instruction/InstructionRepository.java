package fr.ramatellier.clonewar.instruction;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface InstructionRepository extends ReactiveCrudRepository<Instruction, Long> {}