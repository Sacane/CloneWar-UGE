package fr.ramatellier.clonewar.instruction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends CrudRepository<Instruction, Long> {}