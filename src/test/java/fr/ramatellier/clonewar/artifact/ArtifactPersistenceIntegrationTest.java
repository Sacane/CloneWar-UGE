package fr.ramatellier.clonewar.artifact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;
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
        artifact = new Artifact("artifact123", "testArtifact1.jar", LocalDate.now(), null, null);
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
        var artifact2 = new Artifact("artifact123", "testArtifact1.jar", LocalDate.now(), null, null);
        artifactRepository.save(artifact2);
        assertThrows(JpaSystemException.class, () -> artifactRepository.count());
    }

}
