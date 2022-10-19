package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.Hasher;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="instruction")
public class Instruction {

    private static final long serialUUID = 2233498293L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "startLine", nullable = false)
    private int lineNumberStart;


    @Column(name = "content")
    private String content; //Each piece of instruction is separate with \n


    @Column(name = "hash_value")
    private long hash;

    public Instruction(){}
    public Instruction(int lineNumberStart, String content){
        this.lineNumberStart = lineNumberStart;
        this.content = content;
        this.hash = Hasher.hashInstruction(content);
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
