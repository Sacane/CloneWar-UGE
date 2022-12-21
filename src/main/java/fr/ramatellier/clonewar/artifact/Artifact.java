package fr.ramatellier.clonewar.artifact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ramatellier.clonewar.EntitySerializable;
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

    @Column(unique = true)
    private String name;

    private String url;
    private String urlSrc;

//    @Lob
    private byte[] jarFile;

    private byte[] srcFile;

//    @Lob
//    private byte[] srcFile;
    private LocalDate inputDate;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonIgnore
    private final List<Instruction> instructions = new ArrayList<>();

    public Artifact(){}
    public Artifact(String name, String url, String urlSrc, LocalDate inputDate, byte[] jarFile, byte[] srcFile){
        this.name= name;
        this.url = url;
        this.urlSrc = urlSrc;
        this.inputDate = inputDate;
        this.jarFile = jarFile;
        this.srcFile = srcFile;
    }
//    public Artifact(String name, String url, LocalDate inputDate){
//        this(name, url, inputDate, null);
//    }

    public void addAllInstructions(List<Instruction> instructions) {
        this.instructions.addAll(instructions);
    }

    public List<Instruction> instructions() {
        return instructions;
    }

    public UUID id(){
        return id;
    }

    public byte[] jarfile(){
        return jarFile;
    }
    public byte[] srcFile(){
        return srcFile;
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
        return new ArtifactDTO(id.toString(), name, inputDate.toString(), url, urlSrc);
    }
}
