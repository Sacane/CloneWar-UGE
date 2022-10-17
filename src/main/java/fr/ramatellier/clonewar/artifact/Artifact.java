package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.instruction.Instruction;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Artifact {

    @Id
    private UUID id;

    @NotBlank
    private String name;
    @NotBlank
    private String url;
    @NotBlank
    private Date inputDate;

    @OneToMany
    private List<Instruction> instructions = new ArrayList<>();

    public Artifact(){}
    public Artifact(@NotBlank String name, @NotBlank String url, @NotBlank String inputDate){

    }

}
