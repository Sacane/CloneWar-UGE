package fr.ramatellier.clonewar.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ramatellier.clonewar.instruction.Instruction;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="artifact")
public class Artifact {

    @Id
    @GeneratedValue
    private UUID id;


    private String name;

    private String url;

    private LocalDate inputDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Instruction> instructions = new ArrayList<>();

    public Artifact(){}
    public Artifact(String name, String url, LocalDate inputDate){
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
    public LocalDate inputDate(){
        return inputDate;
    }

}
