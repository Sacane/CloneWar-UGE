package fr.ramatellier.clonewar.artifact;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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
            new Artifact("artifact1", "artifact1.jar", LocalDate.now()),
            new Artifact("artifact2", "artifact2.jar", LocalDate.now()),
            new Artifact("artifact3", "artifact3.jar", LocalDate.now()),
            new Artifact("artifact4", "artifact4.jar", LocalDate.now()),
            new Artifact("artifact5", "artifact5.jar", LocalDate.now())
    );


    private boolean artifactsMatches(ArtifactDTO a1, ArtifactDTO a2){
        return a1.name().equals(a2.name()) && a1.url().equals(a2.url()) && a1.date().equals(a2.date());
    }


    @Test
    public void testEndpointSavingArtifact(){
        String randomUUID = UUID.randomUUID().toString();
        ArtifactDTO dto = new ArtifactDTO(randomUUID, "artifact0", LocalDate.now().toString(), "./repo/test/artifact.jar");
        webClient.post()
                .uri("/api/artifact/save")
                .body(Mono.just(dto), ArtifactDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtifactDTO.class)
                .value(artifactDTO -> artifactsMatches(artifactDTO, dto));
    }

    @Test
    public void testEndpointFindAllArtifacts(){
        repository.saveAll(artifacts);
        webClient.get()
                .uri("api/artifacts")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(ArtifactDTO.class);
    }
}
