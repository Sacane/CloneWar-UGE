package fr.ramatellier.clonewar.exception;

public class InvalidJarFormatException extends RuntimeException{

    public InvalidJarFormatException(Throwable t){
        super(t);
    }
    public InvalidJarFormatException(String message){
        super(message);
    }
    public InvalidJarFormatException(String message, Throwable t){
        super(message, t);
    }
}
