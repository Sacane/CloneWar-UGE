package fr.ramatellier.clonewar.artifact;

import fr.ramatellier.clonewar.exception.PomNotSameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArtifactServiceTest {

    private static String SAMPLE_PATH = System.getProperty("user.dir") + "/src/test/resources/samples/";

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
        var graphMainJar = Path.of(SAMPLE_PATH + "GraphMain.jar");
        var graphSrcJar = Path.of(SAMPLE_PATH + "GraphSrc.jar");
        var graphMainContent = Files.readAllBytes(graphMainJar);
        var graphSrcContent = Files.readAllBytes(graphSrcJar);

        Assertions.assertDoesNotThrow(() -> ArtifactService.checkSame(graphSrcContent, graphMainContent));
    }

    @Test
    public void checkSameShouldThrowsIfPomAreNotSame() throws IOException {
        var graphMainJar = Path.of(SAMPLE_PATH + "GraphMain.jar");
        var graphSrcJar = Path.of(SAMPLE_PATH + "jaxbSrc.jar");
        var mainBytes = Files.readAllBytes(graphMainJar);
        var srcBytes = Files.readAllBytes(graphSrcJar);
        Assertions.assertThrows(PomNotSameException.class, () -> ArtifactService.checkSame(srcBytes, mainBytes));
    }

    @Test
     @Timeout(1000)
    public void checkSameShouldNotTakeTooLongEvenForBigProjects() throws IOException{
        var content = setup("guavaMain.jar", "guavaSrc.jar");
        Assertions.assertDoesNotThrow(() -> ArtifactService.checkSame(content.srcBytes, content.mainBytes));
    }
}
