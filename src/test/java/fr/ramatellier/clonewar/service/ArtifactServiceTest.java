package fr.ramatellier.clonewar.service;

import fr.ramatellier.clonewar.exception.PomNotSameException;
import fr.ramatellier.clonewar.service.ArtifactService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArtifactServiceTest {

    private static final String SAMPLE_PATH = System.getProperty("user.dir") + "/src/test/resources/samples/";

    private record MainSrcBytes(byte[] mainBytes, byte[] srcBytes){}

    private MainSrcBytes setup(String jarMain, String jarSrc) throws IOException {
        var mainJarPath = Path.of(SAMPLE_PATH + jarMain);
        var srcJarPath = Path.of(SAMPLE_PATH + jarSrc);
        var mainContent = Files.readAllBytes(mainJarPath);
        var srcContent = Files.readAllBytes(srcJarPath);
        return new MainSrcBytes(mainContent, srcContent);
    }
    @Test
    public void checkSameTest() throws IOException {
        var content = setup("GraphMain.jar", "GraphSrc.jar");
        Assertions.assertDoesNotThrow(() -> ArtifactService.checkSame(content.srcBytes, content.mainBytes));
    }

    @Test
    public void checkSameShouldThrowsIfPomAreNotSame() throws IOException {
        var content = setup("GraphMain.jar", "jaxbSrc.jar");
        Assertions.assertThrows(PomNotSameException.class, () -> ArtifactService.checkSame(content.srcBytes, content.mainBytes));
    }

    @Test
    @Timeout(1000)
    public void checkSameShouldNotTakeTooLongEvenForBigProjects() throws IOException{
        var content = setup("guavaMain.jar", "guavaSrc.jar");
        Assertions.assertDoesNotThrow(() -> ArtifactService.checkSame(content.srcBytes, content.mainBytes));
    }
}
