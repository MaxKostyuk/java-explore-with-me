package ru.practicum.mainsvc.exception;

public class ActionForbiddenException extends RuntimeException {

    public ActionForbiddenException(String message) {
        super(message);
    }
}
