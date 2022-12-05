package fr.ramatellier.clonewar.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class JarReaderTest {

    @Test
    public void storeShouldNotThrowAnyException(){
        var jar = new File("./src/test/resources/Seq.jar");
        var bytes = assertDoesNotThrow(() -> Files.readAllBytes(jar.toPath()));
        var reader = new JarReader(bytes);
        assertDoesNotThrow(() -> AsmParser.printStream(reader.toPath("path.jar")));
        reader.delete();
    }

}
