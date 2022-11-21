package fr.ramatellier.clonewar.artifact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ramatellier.clonewar.EntitySerializable;
import fr.ramatellier.clonewar.artifact.dto.ArtifactDTO;
import fr.ramatellier.clonewar.instruction.Instruction;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="artifact")
public class Artifact implements EntitySerializable<ArtifactDTO> {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    private String url;

//    @Lob
    private byte[] jarFile;

//    @Lob
//    private byte[] srcFile;
    private LocalDate inputDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private final List<Instruction> instructions = new ArrayList<>();

    public Artifact(){}
    public Artifact(String name, String url, LocalDate inputDate, byte[] jarFile){
        this.name= name;
        this.url = url;
        this.inputDate = inputDate;
        this.jarFile = jarFile;
    }
//    public Artifact(String name, String url, LocalDate inputDate){
//        this(name, url, inputDate, null);
//    }

    public void addAllInstructions(List<Instruction> instructions) {
        this.instructions.addAll(instructions);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public UUID id(){
        return id;
    }

    public byte[] jarfile(){
        return jarFile;
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

    @Override
    public ArtifactDTO toDto() {
        return new ArtifactDTO(id.toString(), name, inputDate.toString(), url, jarFile);
    }
}
