package fr.ramatellier.clonewar.persistence.artifact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ramatellier.clonewar.EntitySerializable;
import fr.ramatellier.clonewar.instruction.Instruction;
import fr.ramatellier.clonewar.rest.artifact.ArtifactDTO;
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

    private String version;

//    @Lob
    private byte[] jarFile;

    private byte[] srcFile;

    private LocalDate inputDate;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonIgnore
    private final List<Instruction> instructions = new ArrayList<>();

    public Artifact() {}

    public Artifact(String name, String url, String urlSrc, LocalDate inputDate, byte[] jarFile, byte[] srcFile, String version){
        this.name= name;
        this.url = url;
        this.urlSrc = urlSrc;
        this.inputDate = inputDate;
        this.jarFile = jarFile;
        this.srcFile = srcFile;
        this.version = version;
    }

    /**
     * Take a list of instructions and all these instructions to the current artifact
     * @param instructions List of instructions
     */
    public void addAllInstructions(List<Instruction> instructions) {
        this.instructions.addAll(instructions);
    }

    /**
     * Getter for instructions field
     * @return The list of instructions of the current artifact, of type list of Instruction
     */
    public List<Instruction> instructions() {
        return instructions;
    }

    /**
     * Getter for id field
     * @return The id of the current artifact, of type UUID
     */
    public UUID id() {
        return id;
    }

    public byte[] jarfile() {
        return jarFile;
    }

    /**
     * Getter for the srcFile field
     * @return The srcFile of the current artifact, of type array of bytes
     */
    public byte[] srcFile() {
        return srcFile;
    }

    /**
     * Getter for the name field
     * @return The name of the current artifact, of type String
     */
    public String name() {
        return name;
    }

    /**
     * Getter for the url field
     * @return The url of the current artifact, of type String
     */
    public String url() {
        return url;
    }

    public LocalDate inputDate() {
        return inputDate;
    }

    /**
     * Getter for the version field
     * @return The version of the current artifact, of type String
     */
    public String version() {
        return version;
    }

    /**
     * This method will create an ArtifactDTO which will be the representation of the current artifact but with all his fields represents by String
     * @return The representation of the current artifact with an ArtifactDTO
     */
    @Override
    public ArtifactDTO toDto() {
        return new ArtifactDTO(id.toString(), name, inputDate.toString(), url, urlSrc, version);
    }
}
