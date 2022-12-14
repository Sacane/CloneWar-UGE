package fr.ramatellier.clonewar.exception;

public class PomNotFoundException extends RuntimeException{
    private static final String MESSAGE = "No pom.xml found in the given jar, please make sure it contains .java and pom.xml file";
    public PomNotFoundException(Throwable t){
        super(t);
    }
    public PomNotFoundException(){
        super(MESSAGE);
    }
    public PomNotFoundException(String message, Throwable t){
        super(MESSAGE, t);
    }
    public PomNotFoundException(String message){
        super(message);
    }
}
