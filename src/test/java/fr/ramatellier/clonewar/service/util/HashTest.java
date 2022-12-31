package fr.ramatellier.clonewar.service.util;

import fr.ramatellier.clonewar.service.util.Hash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTest {

    @Test
    public void hashFunctionPrecondition(){
        var content = "";
        assertThrows(IllegalArgumentException.class, () -> Hash.hash(content));
        assertThrows(NullPointerException.class, () -> Hash.hash(null));
    }

    @Test
    public void hashShouldMatchWithSameContent(){
        var content = "1029 10\n127 Return";
        assertEquals(Hash.hash(content), Hash.hash("1029 10\n127 Return"));
    }

    @Test
    public void differentContentShouldNotMatch(){
        var content = "1029 10\n127 Return";
        assertNotEquals(Hash.hash(content), Hash.hash("1029 10\n127 Returns"));
        assertNotEquals(Hash.hash(content), Hash.hash("1028 10\n127 Return"));
        assertNotEquals(Hash.hash(content), Hash.hash("1029 11\n127 Return"));
        assertNotEquals(Hash.hash(content), Hash.hash("1029 10\n126 Returns"));
    }
}
