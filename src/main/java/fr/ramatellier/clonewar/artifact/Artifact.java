package fr.ramatellier.clonewar.artifact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ramatellier.clonewar.instruction.Instruction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Artifact {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String name;
    @NotBlank
    private String url;
    @NotBlank
    private Date inputDate;

    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Instruction> instructions = new ArrayList<>();

    public Artifact(){}
    public Artifact(@NotBlank String name, @NotBlank String url, @NotBlank Date inputDate){
        this.name= name;
        this.url = url;
        this.inputDate = inputDate;
    }

    public UUID id(){
        return id;
    }
    public String name(){
        return name;
    }
    public String url(){
        return url;
    }
    public Date inputDate(){
        return inputDate;
    }

}
