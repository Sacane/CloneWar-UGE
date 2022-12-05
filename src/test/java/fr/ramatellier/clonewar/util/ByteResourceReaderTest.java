package fr.ramatellier.clonewar.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ByteResourceReaderTest {

    @Test
    public void preconditions(){
        assertThrows(NullPointerException.class, () -> new ByteResourceReader(null));
        assertThrows(NullPointerException.class, () -> new ByteResourceReader(new byte[10]).consumeReader(null));
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
                throw new AssertionError();
            }
        });
        var array = list.toArray();
        assertArrayEquals(array, attempt);
    }
}
