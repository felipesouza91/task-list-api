package dev.fsantana.list_manager.domain.execption;

public class AppEntityNotFound extends  RuntimeException{

    public AppEntityNotFound(String message) {
        super(message);
    }
}

