package fr.ramatellier.clonewar.util;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class ByteResourceReader {
    private final byte[] bytes;
    private static int id;
    private final String readerId;
    private ModuleReference finder;
    private JarReader jarReader;
    private boolean isInit;

    public ByteResourceReader(byte[] bytes) {
        this.bytes = Objects.requireNonNull(bytes);
        this.readerId = "readerNo" + id + ".jar";
        id++;
    }

    private void builderFinderFromBytes() {
        isInit = true;
        this.jarReader = new JarReader(bytes);
        var path = jarReader.toPath(readerId);
        finder = ModuleFinder.of(path).findAll().stream().findFirst().orElseThrow();
    }

    /**
     * Method that will apply the given consumer to all the reader
     * @param consumer The consumer we want to apply
     * @throws IOException The method open of ModuleReader can throw an IOException
     */
    public void consumeReader(Consumer<? super ModuleReader> consumer) throws IOException {
        Objects.requireNonNull(consumer);
        if(!isInit) builderFinderFromBytes();
        try(var reader = finder.open()) {
            consumer.accept(reader);
        }
        jarReader.delete();
    }

    /**
     * Method that will apply a function to a reader and then delete it
     * @param fun The function we want to apply to the reader
     * @return The result of the function on the reader
     * @param <T> The return type of the function is a parameterized type
     * @throws IOException The method open of ModuleReader can throw an IOException
     */
    public <T> T retrieveFromReader(Function<? super ModuleReader, ? extends T> fun) throws IOException {
        Objects.requireNonNull(fun);
        if(!isInit) builderFinderFromBytes();
        try(var reader= finder.open()) {
            return fun.apply(reader);
        } finally {
            jarReader.delete();
        }
    }

    /**
     * Method that will search the content of a specified file
     * @param filename The name of the file
     * @return An Optional with the content of this file or else he will be empty
     * @throws IOException The method retrieveFromReader can throw an IOException
     */
    public Optional<String> searchForFileContent(String filename) throws IOException {
        Objects.requireNonNull(filename);
        if(!isInit) builderFinderFromBytes();
        return retrieveFromReader(r -> {
            try{
                for(var fn: (Iterable<String>) r.list()::iterator) {
                    if(fn.contains(filename)) {
                        var md = r.open(fn).orElseThrow();
                        var scan = new Scanner(md).useDelimiter("\\A");
                        var content = scan.hasNext() ? scan.next() : "";
                        if(content.equals("")) return Optional.empty();
                        return Optional.of(content);
                    }
                }
                return Optional.empty();
            }catch (IOException e){
                return Optional.empty();
            }
        });
    }
}