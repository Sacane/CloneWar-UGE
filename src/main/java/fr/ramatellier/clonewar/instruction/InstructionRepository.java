package fr.ramatellier.clonewar.instruction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstructionRepository extends CrudRepository<Instruction, UUID> {}