package fr.ramatellier.clonewar.util;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class JarReader {
    private final byte[] fluxJar;
    private boolean hasAlreadyWrite;
    private File file;

    public JarReader(byte[] fluxJar) {
        this.fluxJar = Objects.requireNonNull(fluxJar);
    }

    private void store(String name) {
        var file = new File(name);
        try(var os = new FileOutputStream(file)){
            os.write(fluxJar);
            hasAlreadyWrite = true;
            this.file = file;
        } catch (IOException e) {
            throw new AssertionError("This error could not occur");
        }
    }

    /**
     * Method that create a file if we give the path of a jar file
     * @param path The path to the jar file
     * @return The path to the new file we created with store()
     */
    public Path toPath(String path) {
        if(!path.endsWith(".jar")) throw new IllegalArgumentException("Should take a jarPath");
        store(path);
        return file.toPath();
    }

    /**
     * Method that delete the JarReader if his file can be deleted and if he already has been written
     */
    public void delete() {
        if(!hasAlreadyWrite) throw new IllegalStateException("You can't close a reader while the file is not open");
        if(!file.delete()) throw new IllegalStateException("This file could not be deleted");
    }
}
