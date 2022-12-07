package fr.ramatellier.clonewar.exception;

public class ArtifactNotFoundException extends RuntimeException{
    private static final String MESSAGE = "No Artefact found in database";
    public ArtifactNotFoundException(Throwable t){
        super(t);
    }
    public ArtifactNotFoundException(){
        super(MESSAGE);
    }
    public ArtifactNotFoundException(String message, Throwable t){
        super(MESSAGE, t);
    }
}
