package ar.com.encogas.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}