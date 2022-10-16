package fr.ramatellier.clonewar.instruction;

import fr.ramatellier.clonewar.util.Hasher;

public class Instruction {

    private final int lineNumberStart;
    private final String content; //Each piece of instruction is separate with spaces
    private final long hash;

    public Instruction(int lineNumberStart, String content){
        this.lineNumberStart = lineNumberStart;
        this.content = content;
        this.hash = Hasher.hashInstruction(content);
    }
}
