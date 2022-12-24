package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.exception.InvalidJarException;
import fr.ramatellier.clonewar.exception.PomNotFoundException;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PomExtractor {

    private final String pom;

    public enum XMLObject{
        CONTRIBUTORS, ARTIFACT_ID, VERSION
    }

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

    static Optional<String> extract(String pomContent, XMLObject xml){
        var tag = xmlToString(xml);
        var pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
        var inPattern = Pattern.compile("<[^/](.*?)>", Pattern.DOTALL);
        var outPattern = Pattern.compile("</(.*?)>", Pattern.DOTALL);
        int depth = 0;
        var lines = pomContent.split("\n");
        for(var line : lines){
            var trim = line.trim();
            if(trim.startsWith("<?xml") || !trim.startsWith("<")) continue;
            if(trim.startsWith("<project")) {
                depth++;
                continue;
            }
            var m = pattern.matcher(trim);
            var out = outPattern.matcher(trim);
            var in = inPattern.matcher(trim);
            if(in.find()) {
                depth++;
            }
            if(out.find()) depth--;
            if(m.find() && depth == 1) return Optional.of(m.group(1));
        }
        return Optional.empty();
    }
    static String xmlToString(XMLObject xmlObject){
        return switch(xmlObject){
            case VERSION -> "version";
            case CONTRIBUTORS -> "name";
            case ARTIFACT_ID -> "artifactId";
        };
    }
    private static Optional<String> xmlContent(XMLObject obj, String pomContent){
        return switch(obj){
            case CONTRIBUTORS -> Optional.empty();
            case VERSION -> extract(pomContent, XMLObject.VERSION);
            case ARTIFACT_ID -> extract(pomContent, XMLObject.ARTIFACT_ID);
        };
    }

    public static Optional<String> retrieveAttribute(byte[] srcContent, XMLObject object) throws IOException {
        var reader = new ByteResourceReader(srcContent);
        return reader.retrieveFromReader(r -> {
            try {
                for(var filename: (Iterable<String>) r.list()::iterator){
                    if(filename.contains("pom.xml")){
                        var md = r.open(filename).orElseThrow();
                        var scan = new Scanner(md).useDelimiter("\\A");
                        var content = scan.hasNext() ? scan.next() : "";
                        if(content.equals("")) return Optional.empty();
                        return xmlContent(object, content);
                    }
                }
                return Optional.empty();
            } catch (IOException e) {
                throw new InvalidJarException(e.getCause());
            }
        });
    }
}
