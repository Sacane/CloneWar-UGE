package fr.ramatellier.clonewar.persistence.Instruction;

import fr.ramatellier.clonewar.service.util.Hash;
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
    public Instruction(String filename, int lineNumberStart, String content){
        this.filename = filename;
        this.lineNumberStart = lineNumberStart;
        this.content = content;
        this.hash = Hash.hash(content);
    }

    public Instruction(String filename, int lineNumberStart, String content, long hash) {
        this.filename = filename;
        this.lineNumberStart = lineNumberStart;
        this.content = content;
        this.hash = hash;
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


    @Override
    public String toString() {
        return "FILE NAME IS " + filename + " FIRST LINE IS " + lineNumberStart + "\n" + content;
    }

}
