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

    @Column(name="order_entry")
    private int order;
    @Column(name = "hash_value")
    private long hash;

    public Instruction(){}
    public Instruction(String filename, int lineNumberStart, String content, int order){
        this.filename = filename;
        this.lineNumberStart = lineNumberStart;
        this.content = content;
        this.hash = Hasher.hash(content);
        this.order = order;
    }

    public String filename() {
        return filename;
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

    public int order() {
        return order;
    }

    @Override
    public String toString() {
        return "FILE NAME IS " + filename + " FIRST LINE IS " + lineNumberStart + "\n" + content;
    }

}
