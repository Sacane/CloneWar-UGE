package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.artifact.Artifact;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
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

    public static Optional<String> retrieveArtifactFromContent(String pomContent){
        if(!pomContent.startsWith("<project") || !pomContent.endsWith("</project>")) throw new IllegalArgumentException("This content is not a pom content");
        var pattern = Pattern.compile("<artifactId>(.*?)</artifactId>", Pattern.DOTALL);
        Matcher m;
        var lines = pomContent.split("\n");
        for(var line : lines){
            m = pattern.matcher(line);
            if(m.find()){
                return Optional.of(m.group(1));
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getProjectArtifactId(byte[] srcContent) throws IOException {
        var reader = new ByteResourceReader(srcContent);
        return reader.retrieveFromReader(r -> {
            try {

                for(var filename: (Iterable<String>) r.list()::iterator){
                    if(filename.contains("pom.xml")){
                        var md = r.open(filename).orElseThrow();
                        Scanner scan = new Scanner(md).useDelimiter("\\A");
                        String result = scan.hasNext() ? scan.next() : "";
                        if(result.equals("")) return Optional.empty();
                        String s = PomExtractor.retrieveArtifactFromContent(result).get();
                        return Optional.of(s);
                    }
                }
                return Optional.empty();
            } catch (IOException e) {
                throw new AssertionError();
            }
        });
    }
}