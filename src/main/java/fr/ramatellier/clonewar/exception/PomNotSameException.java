package fr.ramatellier.clonewar.exception;

public class PomNotSameException extends RuntimeException{
    public PomNotSameException(Throwable r, String s){
        super(s, r);
    }
}
