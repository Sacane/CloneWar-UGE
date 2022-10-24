package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.Hasher;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
public class Instruction {

    @Id
    @GeneratedValue
    private UUID id;

    private String filename;
    @NotBlank
    private int lineNumberStart;

    @NotBlank
    private String content; //Each piece of instruction is separate with \n

    @NotBlank
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

    @Override
    public String toString() {
        return "FIRST LINE IS " + lineNumberStart + "\n" + content;
    }
}
