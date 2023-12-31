package fr.ramatellier.clonewar.rest;

import fr.ramatellier.clonewar.persistence.artifact.Artifact;
import fr.ramatellier.clonewar.persistence.artifact.ArtifactRepository;
import fr.ramatellier.clonewar.rest.artifact.ArtifactDTO;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ArtifactWebLayerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ArtifactRepository repository;

    private static final List<Artifact> artifacts = List.of(
            new Artifact("artifact1", "artifact1.jar", LocalDate.now(), null, null, "0.1", "Ramaroson"),
            new Artifact("artifact2", "artifact2.jar",  LocalDate.now(), null, null, "0.1", "Ramaroson"),
            new Artifact("artifact3", "artifact3.jar", LocalDate.now(), null, null, "0.1", "Ramaroson"),
            new Artifact("artifact4", "artifact4.jar",  LocalDate.now(), null, null, "0.1", "Ramaroson"),
            new Artifact("artifact5", "artifact5.jar", LocalDate.now(), null, null, "0.1", "Ramaroson")
    );


    private boolean artifactsMatches(ArtifactDTO a1, ArtifactDTO a2){
        return a1.name().equals(a2.name()) && a1.url().equals(a2.url()) && a1.date().equals(a2.date());
    }


    @Test
    public void testEndpointFindAllArtifacts(){
        repository.saveAll(artifacts);
        webClient.get()
                .uri("api/artifacts")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(ArtifactDTO.class)
                .value(List::size, IsEqual.equalTo(5))
                .value(list -> list.stream().anyMatch(artifactDTO -> artifactsMatches(artifactDTO, new ArtifactDTO(UUID.randomUUID().toString(), "artifact1", LocalDate.now().toString(), "artifact1.jar", null, "0.1"))), IsEqual.equalTo(true))
                .value(list -> list.stream().anyMatch(artifactDTO -> artifactsMatches(artifactDTO, new ArtifactDTO(UUID.randomUUID().toString(), "artifact2", LocalDate.now().toString(), "artifact2.jar", null, "0.1"))), IsEqual.equalTo(true))
                .value(list -> list.stream().anyMatch(artifactDTO -> artifactsMatches(artifactDTO, new ArtifactDTO(UUID.randomUUID().toString(), "artifact3", LocalDate.now().toString(), "artifact3.jar", null, "0.1"))), IsEqual.equalTo(true))
                .value(list -> list.stream().anyMatch(artifactDTO -> artifactsMatches(artifactDTO, new ArtifactDTO(UUID.randomUUID().toString(), "artifact4", LocalDate.now().toString(), "artifact4.jar", null, "0.1"))), IsEqual.equalTo(true))
                .value(list -> list.stream().anyMatch(artifactDTO -> artifactsMatches(artifactDTO, new ArtifactDTO(UUID.randomUUID().toString(), "artifact5", LocalDate.now().toString(), "artifact5.jar", null, "0.1"))), IsEqual.equalTo(true));
    }
}
