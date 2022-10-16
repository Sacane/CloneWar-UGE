package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.instruction.Instruction;

import java.util.List;

public class Hasher {

    private long hash(String str){
        return 0L;
    }
    public static long hashInstruction(String content){
        int limit = 1_000_007;
        int p = 31;
        var toHash = content.replaceAll("\n", ",");
        var hashValue = 0L;
        var charArray = toHash.toCharArray();
        var pow = 1L;
        for(int i = 0; i < toHash.length(); i++){
            hashValue = (hashValue + (charArray[i] - 'a' + 1) * pow) % limit;
            pow = (pow* p) % limit;
        }
        return hashValue;
    }
}
