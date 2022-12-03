package fr.ramatellier.clonewar.util;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class JarReader {
    private final byte[] fluxJar;
    private boolean hasAlreadyWrite;
    private File file;
    private static int storingId;

    public JarReader(byte[] fluxJar){
        this.fluxJar = Objects.requireNonNull(fluxJar);
    }

    public void store() {
        var tmpName = "storingTmp" + storingId + ".jar";
        var file = new File(tmpName);
        try(var os = new FileOutputStream(file)){
            os.write(fluxJar);
            hasAlreadyWrite = true;
            storingId++;;;;;;
            this.file = file;
        } catch (IOException e) {
            throw new AssertionError("This error could not occur");
        }
    }

    public Path toPath(){
        return file.toPath();
    }

    public void delete(){
        if(!hasAlreadyWrite) throw new IllegalStateException("You can't close a reader while the file is not open");
        if(!file.delete()) throw new IllegalStateException("This file could not be deleted");
    }

}
