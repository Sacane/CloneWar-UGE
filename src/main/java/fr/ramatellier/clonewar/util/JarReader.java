package fr.ramatellier.clonewar.util;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class JarReader {
    private final byte[] fluxJar;
    private boolean hasAlreadyWrite;
    private File file;

    public JarReader(byte[] fluxJar){
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

    public Path toPath(String path){
        if(!path.endsWith(".jar")) throw new IllegalArgumentException("Should take a jarPath");
        store(path);
        return file.toPath();
    }

    public File toFile(){
        if(!hasAlreadyWrite) store("tmp.jar");
        return file;
    }

    public void delete(){
        if(!hasAlreadyWrite) throw new IllegalStateException("You can't close a reader while the file is not open");
        if(!file.delete()) throw new IllegalStateException("This file could not be deleted");
    }

}
