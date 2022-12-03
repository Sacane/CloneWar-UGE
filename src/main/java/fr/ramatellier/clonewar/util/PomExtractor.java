package fr.ramatellier.clonewar.util;

import java.io.*;
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

    /**
     * Search for the first artifactId of the project, this one is considered as the id of the whole project.
     * @return An optional of the artifact Id, Empty if there isn't any artifact Id in the pom.xml
     * @throws IOException if an error has occur while opening the file
     */
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
