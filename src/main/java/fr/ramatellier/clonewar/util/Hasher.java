package fr.ramatellier.clonewar.util;

import java.util.Objects;

public final class Hasher {

    private final static int LIMIT = 1_000_007;
    private final static int BASE = 31;

//    public static long hashInstruction(String content){
//        Objects.requireNonNull(content);
//        if(content.equals("")) throw new IllegalArgumentException("Can't hash an empty content");
//        var toHash = content.replaceAll("\n", ",");
//        var hashValue = 0L;
//        var charArray = toHash.toCharArray();
//        var pow = 1L;
//        for(int i = 0; i < toHash.length(); i++){
//            hashValue = (hashValue + (charArray[i] - 'a' + 1 + LIMIT) * pow) % LIMIT;
//            pow = (pow* BASE) % LIMIT;
//        }
//        return hashValue;
//    }

    /**
     * Hash an instruction using the rolling-hash strategy.
     * @param instruction to hash
     * @return the hash value of the given instruction
     */
    public static long hash(String instruction){
        Objects.requireNonNull(instruction);
        if(instruction.equals("")) throw new IllegalArgumentException("Can't hash an empty instruction");
        var toHash = instruction.split("\n");
        var pow = 1L;
        var value = 0L;
        for(var insPart : toHash){
            value = (value + (insPart.hashCode()) + LIMIT * pow) % LIMIT;
            pow = (pow * BASE) % LIMIT;
        }
        return value;
    }
}
