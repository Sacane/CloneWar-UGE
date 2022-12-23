package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.util.JarReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ArtifactPersistenceIntegrationTest {

    @Autowired
    private ArtifactRepository artifactRepository;


    private Artifact artifact;

    @BeforeEach
    public void setupArtifacts(){
        artifact = new Artifact("artifact123", "testArtifact1.jar", "testArtifact1.jar", LocalDate.now(), null, null);
        artifactRepository.save(artifact);
    }

    private void hugeSetup() throws IOException {
        Path path = Path.of(System.getProperty("user.dir") + "/src/test/resources/samples/guavaMain.jar");
        var artifact = new Artifact("huge", "huge.jar", "hugesrc.jar", LocalDate.now(), Files.readAllBytes(path), null);
        artifactRepository.save(artifact);
    }

    @Test
    public void testSaveArtifact(){
        var artifacts = artifactRepository.findAll();
        assertThat(artifacts).isNotNull();
        assertThat(artifacts).isNotEmpty();
        assertThat(artifacts).containsExactly(artifact);
    }

    @Test
    public void uniqueConstraintShouldBeEffective(){
        var artifact2 = new Artifact("artifact123", "testArtifact1.jar","testArtifact1.jar", LocalDate.now(), null, null);
        artifactRepository.save(artifact2);
        assertThrows(JpaSystemException.class, () -> artifactRepository.count());
    }


    @Test
    public void hugeFileEntryTest() throws IOException {
        hugeSetup();
        var artifact = assertDoesNotThrow(() -> artifactRepository.findByName("huge"));
        assertEquals("huge", artifact.name());
    }

}
