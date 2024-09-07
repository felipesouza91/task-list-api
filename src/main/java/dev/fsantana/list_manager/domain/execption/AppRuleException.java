package dev.fsantana.list_manager.domain.execption;

public class AppRuleException extends  RuntimeException{

    public AppRuleException(String message) {
        super(message);
    }
}
