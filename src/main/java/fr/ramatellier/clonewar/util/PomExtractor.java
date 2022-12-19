package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.exception.InvalidJarException;
import fr.ramatellier.clonewar.exception.PomNotFoundException;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
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
            throw new PomNotFoundException();
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

    static Optional<String> retrieveArtifactFromContent(String pomContent){
//        if(!pomContent.endsWith("</project>")) throw new IllegalArgumentException("This content is not a pom content");
//        var pattern = Pattern.compile("<artifactId>(.*?)</artifactId>", Pattern.DOTALL);
//        var entryPattern = Pattern.compile("<(.*?)>", Pattern.DOTALL);
//        var outPattern = Pattern.compile("</(.*?)>", Pattern.DOTALL);
//        int depth = 0;
//        var lines = pomContent.split("\n");
//        for(var line : lines){
//            var m = pattern.matcher(line);
//            if(m.find() && depth == 1){
//                return Optional.of(m.group(1));
//            }
//            m = outPattern.matcher(line.trim());
//            if(m.matches()) {
//                depth--;
//                continue;
//            }
//            var m2 = entryPattern.matcher(line);
//            if(m.find() && m2.find()) continue;
//            if(m2.find() && !m.find()) depth++;
//        }
//        return Optional.empty();
        if(!pomContent.trim().endsWith("</project>")) throw new IllegalArgumentException("This content is not a pom content");
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
        System.out.println("GetProjectArtifactId");
        return reader.retrieveFromReader(r -> {
            try {
                System.out.println("In reader");
                for(var filename: (Iterable<String>) r.list()::iterator){
                    if(filename.contains("pom.xml")){
                        var md = r.open(filename).orElseThrow();
                        Scanner scan = new Scanner(md).useDelimiter("\\A");
                        String result = scan.hasNext() ? scan.next() : "";
                        if(result.equals("")) return Optional.empty();
                        System.out.println(result);
                        return retrieveArtifactFromContent(result);
                    }
                }
                throw new PomNotFoundException("No pom.xml found in this jar source...");
            } catch (IOException e) {
                throw new InvalidJarException(e.getCause());
            }
        });
    }
}
