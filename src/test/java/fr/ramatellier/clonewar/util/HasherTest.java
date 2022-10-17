package fr.ramatellier.clonewar.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasherTest {

    @Test
    public void hashFunctionPrecondition(){
        var content = "";
        assertThrows(IllegalArgumentException.class, () -> Hasher.hashInstruction(content));
        assertThrows(NullPointerException.class, () -> Hasher.hashInstruction(null));
    }

    @Test
    public void hashShouldMatchWithSameContent(){
        var content = "1029 10\n127 Return";
        assertEquals(Hasher.hashInstruction(content), Hasher.hashInstruction("1029 10\n127 Return"));
    }

    @Test
    public void differentContentShouldNotMatch(){
        var content = "1029 10\n127 Return";
        assertNotEquals(Hasher.hashInstruction(content), Hasher.hashInstruction("1029 10\n127 Returns"));
        assertNotEquals(Hasher.hashInstruction(content), Hasher.hashInstruction("1028 10\n127 Return"));
        assertNotEquals(Hasher.hashInstruction(content), Hasher.hashInstruction("1029 11\n127 Return"));
        assertNotEquals(Hasher.hashInstruction(content), Hasher.hashInstruction("1029 10\n126 Returns"));
    }
}
