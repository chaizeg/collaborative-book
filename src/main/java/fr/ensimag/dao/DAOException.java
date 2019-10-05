package fr.ensimag.dao;

public class DAOException extends RuntimeException{
    private static final long serialVersionUID = 7647287520691159819L;

    public DAOException() {

    }
    public DAOException(String message){
        super(message);
    }
    public DAOException(String message, Throwable cause){
        super(message, cause);
    }
}