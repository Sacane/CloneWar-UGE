package fr.ramatellier.clonewar.util;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ByteResourceReader {
    private final byte[] bytes;
    private static int id;
    private final String readerId;
    private ModuleReference finder;
    private JarReader jarReader;
    private boolean isInit;

    public ByteResourceReader(byte[] bytes){
        this.bytes = Objects.requireNonNull(bytes);
        this.readerId = "readerNo" + id + ".jar";
        id++;
    }

    private void builderFinderFromBytes(){
        isInit = true;
        this.jarReader = new JarReader(bytes);
        var path = jarReader.toPath(readerId);
        finder = ModuleFinder.of(path).findAll().stream().findFirst().orElseThrow();
    }

    public void consumeReader(Consumer<? super ModuleReader> consumer) throws IOException {
        Objects.requireNonNull(consumer);
        if(!isInit) builderFinderFromBytes();
        try(var reader = finder.open()){
            consumer.accept(reader);
        }
        jarReader.delete();
    }

    public <T> T retrieveFromReader(Function<? super ModuleReader, ? extends T> fun) throws IOException {
        Objects.requireNonNull(fun);
        if(!isInit) builderFinderFromBytes();
        try(var reader= finder.open()){
            return fun.apply(reader);
        } finally {
            jarReader.delete();
        }

    }

}
