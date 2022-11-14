# Clonewar

| Developers | Version of document | last update |
| :---------: | :-----------------:| :----------: |
| Ramaroson Rakotomihamina Johan && Tellier Quentin | 0.1 | 14/11/2022|

## 1. About 
Clonewar is a web-app that allow you to analyze and detect clones among multiple java project. 
It works using artifacts that can be build by giving a jar-archive. From here the application will index all the instructions in bytecode of the project. Then you can analyze several artifacts knowing if there is clone between them.

## 2. Architecture
Clonewar is divide in two sub-applications : a back-end for persistence and computation and a front-end for user interface.

*note : this section contains all the information that allows to understand how each 
side of the project can communicate between each others.*

### I. Back-end
In this part, all the computation and logic will be implemented. Such as the Karp-Rabin algorithm which will be used for detecting clones among projects. 

The back-end part has 2 main connexions :
1. With our database.
2. With the outside, the web.


So basically, the first one is alimented by classes which represents controller that represents methods in our api rest, so we will use dto to map json object to our java application. 
And the second one represent the requests and interaction with our database. 

### I. 1 - Entity initialisation and persistence

For example the main entity in this project is the **Artifact** entity. So we will create
multiple class around this entity to manipulate it through the several layers of the application.
Here is an example of the Artifact entity that will be persist in our db :

```java
@Entity
@Table(name="artifact")
public class Artifact implements EntitySerializable<ArtifactDTO> {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    private String url;

    private LocalDate inputDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private final List<Instruction> instructions = new ArrayList<>();

    public Artifact(){}
    public Artifact(String name, String url, LocalDate inputDate){
        this.name= name;
        this.url = url;
        this.inputDate = inputDate;
    }

    public void addAllInstructions(List<Instruction> instructions) {
        for(var instruction: instructions) {
            this.instructions.add(instruction);
        }
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public UUID id(){
        return id;
    }

    public String name(){
        return name;
    }
    public String url(){
        return url;
    }
    public LocalDate inputDate(){
        return inputDate;
    }

    @Override
    public ArtifactDTO toDto() {
        return new ArtifactDTO(id.toString(), name, inputDate.toString(), url);
    }
}
```
We can use the jakarta **@Entity** annotation to specify that this class represent a persistable entity.

Spring allows us to build easily our repositories to apply queries to our database using entities, here is an 
example of the artifact's repository : 
```java
@Repository
public interface ArtifactRepository extends CrudRepository<Artifact, UUID> {}
```
 This repository extends a CrudRepository which contains the main methods such as save to persist an entity, findAll to retrieve all the saved entities etc...

### I. 2 - Transfer our data to the web

So now that we can represent our entity, next step is to allow our application to speak with the web. 
To use it we will create a *controller* that will contain the server's methods. 
Here is an example of controller for the artifacts : 

```java
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ArtifactController {
    
    private static final Path UPLOAD_PATH = Paths.get("./src/main/resources/upload/");
    private final ArtifactService service;
    private static final Logger LOGGER = Logger.getLogger(ArtifactController.class.getName());
    
    public ArtifactController(ArtifactService service){
        this.service = service;
    }

    @GetMapping(path = "/api/artifacts")
    public Flux<ArtifactDTO> retrieveAllArtifacts(){
        LOGGER.info("Starting to retrieve all artifacts in database");
        return service.findAll().delayElements(Duration.ofMillis(150)).map(Artifact::toDto);
    }

    @PostMapping(path="/api/artifact/upload", headers = "content-type=multipart/*")
    public Mono<Void> uploadJarFile(@RequestPart("jar") Mono<FilePart> jarFile){
        LOGGER.info("Attempt to upload a file: ");
        return jarFile
                .doOnNext(fp -> LOGGER.info("Received file : " + fp.filename()))
                .flatMap(fp -> fp.transferTo(UPLOAD_PATH.resolve(fp.filename())))
                .then();
    }

    @PostMapping(path = "/api/artifact/persist")
    public Mono<ArtifactDTO> putArtifacts(@RequestBody ArtifactSaveDTO dto){
        LOGGER.info("Persist an artifact and its instructions");
        try {
            return service.saveArtifactWithInstruction(dto);
        } catch (IOException e) {
            LOGGER.severe("Cannot found jar : " + dto.url());
            return Mono.error(new IOException("No file as " + dto.url() + " found"));
        }
    }
}
```
### II. Front-end


## 3. Techs

|       Back        | Front | Mapping persistence | Database | UI |
|:-----------------:|:-----:|:------:| :--------:|:-------:| 
| Spring 6 Reactive | React | Jakarta | Sqlite | Bulma

