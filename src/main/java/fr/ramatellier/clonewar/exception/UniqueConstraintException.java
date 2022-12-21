package fr.ramatellier.clonewar.exception;

public class UniqueConstraintException extends RuntimeException{

    private static final String MESSAGE = "Unique constraint has been violated";

    public UniqueConstraintException(){
        super(MESSAGE);
    }

    public UniqueConstraintException(Throwable t){
        super(MESSAGE, t);
    }
    public UniqueConstraintException(String message, Throwable t){
        super(message, t);
    }
}
