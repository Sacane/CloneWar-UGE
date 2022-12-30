package fr.ramatellier.clonewar.service.util;

import fr.ramatellier.clonewar.service.util.ByteResourceReader;
import fr.ramatellier.clonewar.service.util.PomExtractor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ByteResourceReaderTest {

    @Test
    public void preconditions(){
        assertThrows(NullPointerException.class, () -> new ByteResourceReader(null));
        assertThrows(NullPointerException.class, () -> new ByteResourceReader(new byte[10]).consumeReader(null));
    }

    @Test
    public void artifactIdExtractedFromSrcJarTest(){
        var pathSrc = Path.of("src/test/resources/samples/SeqSrc.jar");
        var bytes = assertDoesNotThrow(() -> Files.readAllBytes(pathSrc));
        var reader = new ByteResourceReader(bytes);
        assertDoesNotThrow(() -> reader.consumeReader(r -> {
            try {
                String s;
                for(var filename: (Iterable<String>) r.list()::iterator){
                    if(filename.contains("pom.xml")){
                        var md = r.open(filename).orElseThrow();
                        Scanner scan = new Scanner(md).useDelimiter("\\A");
                        String result = scan.hasNext() ? scan.next() : "";
                        s = PomExtractor.extract(result, PomExtractor.XMLObject.ARTIFACT_ID).get();
                        assertEquals("seq", s);
                        break;
                    }
                }
            } catch (IOException e) {
                throw new AssertionError();
            }
        }));
    }

    @Test
    public void readerShouldConsumeProperly() throws IOException {
        var attempt = new String[]{
                "fr/sacane/test/Application.class",
                "fr/sacane/test/Visited.class",
                "fr/sacane/test/VisitedConstructor.class"
        };
        var list = new ArrayList<String>();
        var jar = new File("./src/test/resources/TestJar.jar");
        var bytes = assertDoesNotThrow(() -> Files.readAllBytes(jar.toPath()));
        var reader = new ByteResourceReader(bytes);
        reader.consumeReader(r -> {
            try {
                for(var filename: (Iterable<String>) r.list()::iterator){
                    if(filename.endsWith(".class")){
                        list.add(filename);
                    }
                }
            } catch (IOException e) {
                fail();
            }
        });
        var array = list.toArray();
        assertArrayEquals(array, attempt);
    }

    @Test
    public void searchFileContentTest() throws IOException {
        var path = System.getProperty("user.dir") + "/src/test/resources/samples/";
        var graphMainJar = Path.of(path + "GraphMain.jar");
        var graphSrcJar = Path.of(path + "GraphSrc.jar");
        var graphMainContent = Files.readAllBytes(graphMainJar);
        var graphSrcContent = Files.readAllBytes(graphSrcJar);

        var graphResourceReader = new ByteResourceReader(graphMainContent);
        var graphSrcResourceReader = new ByteResourceReader(graphSrcContent);

        assertTrue(graphSrcResourceReader.searchForFileContent("pom.xml").isPresent());
        assertTrue(graphResourceReader.searchForFileContent("pom.xml").isPresent());
    }
}
