package fr.ramatellier.clonewar.util;

import fr.ramatellier.clonewar.exception.InvalidJarException;
import fr.ramatellier.clonewar.exception.PomNotFoundException;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
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
     * Method that will find the name of the developers in the content of a pom.xml
     * @param lines The content of the pom.xml
     * @return The list of the name of the developers
     */
    public static List<String> retrieveContributors(String[] lines){
        var depth = 0;
        var list = new ArrayList<String>();
        var devsPattern = Pattern.compile("<developers>");
        var outDevPattern = Pattern.compile("</developer>");
        var outDevsPattern = Pattern.compile("</developers>");
        var devPattern = Pattern.compile("<developer>");
        var nameDev = Pattern.compile("<name>(.*?)</name>");
        for(var line : lines){
            var matcher = nameDev.matcher(line);
            if(outDevsPattern.matcher(line).find()){
                break;
            }
            if(devsPattern.matcher(line).find() || devPattern.matcher(line).find()) depth++;
            if(outDevPattern.matcher(line).find()) depth--;
            if(matcher.find() && depth == 2) list.add(matcher.group(1));
        }
        return list;
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

    private static Optional<String> preExtract(String[] lines, Pattern pattern, Pattern inPattern, Pattern outPattern){
        var depth = 0;
        for(var line : lines){
            var trim = line.trim();
            if(trim.startsWith("<?xml") || !trim.startsWith("<")) continue;
            if(trim.startsWith("<project")) {
                depth++;
                continue;
            }
            var m = pattern.matcher(trim);
            if(inPattern.matcher(trim).find()) {
                depth++;
            }
            if(outPattern.matcher(trim).find()) depth--;
            if(m.find() && depth == 1) return Optional.of(m.group(1));
        }
        return Optional.empty();
    }

    static Optional<String> extract(String pomContent, XMLObject xml){
        var tag = xmlToString(xml);
        var pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
        var inPattern = Pattern.compile("<[^/](.*?)>", Pattern.DOTALL);
        var outPattern = Pattern.compile("</(.*?)>", Pattern.DOTALL);
        var lines = pomContent.split("\n");
        return preExtract(lines, pattern, inPattern, outPattern);
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

    /**
     * Generic method to find something in a pom.xml
     * @param srcContent The content of the pom.xml is an array of bytes
     * @param object It will find something in the pom.xml, CONTRIBUTORS for the name of the developers, ARTIFACT_ID for the artifactId and VERSION for the project version
     * @return An Optional of what have been find in the pom.xml, it's empty if there is nothing
     * @throws IOException The method retrieveFromReader can throw an IOException
     */
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