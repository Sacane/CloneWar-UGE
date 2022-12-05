package fr.ramatellier.clonewar.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PomExtractorTest {


    @Test
    public void shouldRetrieveGoodArtifactNameFromPom(){
        var extractor = new PomExtractor("pom.xml");
        var extracted = assertDoesNotThrow(extractor::retrieveArtifactName);
        assertTrue(extracted.isPresent());
        assertEquals("CloneWar", extracted.get());
    }



    @Test
    public void preconditions(){
        assertThrows(NullPointerException.class, () -> new PomExtractor(null));
        assertThrows(IllegalArgumentException.class, () -> new PomExtractor("foo.xml"));
        assertThrows(IllegalArgumentException.class, () -> new PomExtractor("src/main/pom.xml"));
    }
}