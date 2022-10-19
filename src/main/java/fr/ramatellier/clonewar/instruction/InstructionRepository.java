package fr.ramatellier.clonewar.instruction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends CrudRepository<Instruction, Long> {}