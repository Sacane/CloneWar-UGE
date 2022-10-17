package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.Hasher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name="instruction")
public class Instruction {

    private static final long serialUUID = 2233498293L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "filename")
    private String filename;
    @NotBlank
    @Column(name = "startLine")
    private int lineNumberStart;

    @NotBlank
    @Column(name = "content")
    private String content; //Each piece of instruction is separate with \n

    @NotBlank
    @Column(name = "hash_value")
    private long hash;

    public Instruction(){}
    public Instruction(@NotBlank int lineNumberStart, @NotBlank String content){
        this.lineNumberStart = lineNumberStart;
        this.content = content;
        this.hash = Hasher.hashInstruction(content);
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setLineNumberStart(int lineNumberStart) {
        this.lineNumberStart = lineNumberStart;
    }

    public int getLineNumberStart() {
        return lineNumberStart;
    }

    public long hashValue() {
        return hash;
    }

    public String content() {
        return content;
    }

    public UUID id() {
        return id;
    }
}
