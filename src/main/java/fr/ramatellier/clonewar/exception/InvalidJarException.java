package fr.ramatellier.clonewar.exception;

public class InvalidJarException extends RuntimeException{

    private static final String MESSAGE = "The given jar is not valid";

    public InvalidJarException(Throwable t){
        super(t);
    }
    public InvalidJarException(){
        super(MESSAGE);
    }
    public InvalidJarException(String message, Throwable t){
        super(message, t);
    }

}
