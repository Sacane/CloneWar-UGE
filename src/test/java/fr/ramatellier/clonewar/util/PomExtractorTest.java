package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.exception.PomNotFoundException;
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
    public void retrieveArtifactContentTest(){
        String content = """
                <project>
                        <modelVersion>4.0.0</modelVersion>
                        <groupId>fr.ramatellier</groupId>
                        <artifactId>CloneWar</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <name>CloneWar</name>
                        <description>Project allow people to detect plagiarism between two projects</description>
                </project>
                """.trim();
        var artifact = PomExtractor.retrieveArtifactFromContent(content);
        assertTrue(artifact.isPresent());
        assertEquals("CloneWar", artifact.get());
    }

    @Test
    public void shouldNotRetrieveIfDepthIsMoreThan1(){
        String content = """
                <project>
                        <modelVersion>4.0.0</modelVersion>
                        <groupId>fr.ramatellier</groupId>
                        <version>0.0.1-SNAPSHOT</version>
                        <name>CloneWar</name>
                        <description>Project allow people to detect plagiarism between two projects</description>
                        <parent>
                            <artifactId>parentId</artifactId>
                        </parent>
                </project>
                """.trim();
        var res = PomExtractor.retrieveArtifactFromContent(content);
        assertTrue(res.isEmpty());
    }
    @Test
    public void extractorShouldNotRetrieveWhenArtifactIdIsAbsent(){
        String content = """
                <project>
                        <modelVersion>4.0.0</modelVersion>
                        <groupId>fr.ramatellier</groupId>
                        <version>0.0.1-SNAPSHOT</version>
                        <name>CloneWar</name>
                        <description>Project allow people to detect plagiarism between two projects</description>
                </project>
                """.trim();
        var artifact = PomExtractor.retrieveArtifactFromContent(content);
        assertTrue(artifact.isEmpty());
    }

    @Test
    public void extractorShouldRetrieveTheGoodArtifactId(){
        var content = """
                <project>
                        <modelVersion>4.0.0</modelVersion>
                        <groupId>fr.ramatellier</groupId>
                        <version>0.0.1-SNAPSHOT</version>
                        <name>CloneWar</name>
                        <description>Project allow people to detect plagiarism between two projects</description>
                        <parent>
                            <name>Parent</name>
                            <artifactId>ParentId</artifactId>
                        </parent>
                        <child>
                            <name>Child</name>
                        </child>
                        <artifactId>CloneWar</artifactId>
                </project>
                """.trim();
        var res = PomExtractor.retrieveArtifactFromContent(content);
        assertTrue(res.isPresent());
        assertNotEquals("Parent", res.get());
        assertEquals("CloneWar", res.get());
    }

    @Test
    public void preconditions(){
        assertThrows(NullPointerException.class, () -> new PomExtractor(null));
        assertThrows(IllegalArgumentException.class, () -> new PomExtractor("foo.xml"));
        assertThrows(PomNotFoundException.class, () -> new PomExtractor("src/main/pom.xml"));
    }
}