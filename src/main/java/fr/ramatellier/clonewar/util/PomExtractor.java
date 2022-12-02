package fr.ramatellier.clonewar.util;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PomExtractor {

    private final String pom;

    public PomExtractor(String pathToPom) {
        Objects.requireNonNull(pathToPom);
        if(!pathToPom.contains("pom.xml")){
            throw new IllegalArgumentException("This class should take a pom.xml file");
        }
        if(!Path.of(pathToPom).toFile().exists()){
            throw new IllegalArgumentException("this file doesn't exists");
        }
        this.pom = pathToPom;
    }

    public Optional<String> retrieveArtifactName() throws IOException {
        try(var pomReader = new BufferedReader(new FileReader(pom))){
            String line;
            var pattern = Pattern.compile("<artifactId>(.*?)</artifactId>", Pattern.DOTALL);
            Matcher m;
            while ((line = pomReader.readLine()) != null) {
                m = pattern.matcher(line);
                if (m.find()) {
                    return Optional.of(m.group(1));
                }
            }
        }
        return Optional.empty();
    }
}
