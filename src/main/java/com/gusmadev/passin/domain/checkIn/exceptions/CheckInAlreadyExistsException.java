package com.gusmadev.passin.domain.checkIn.exceptions;

public class CheckInAlreadyExistsException extends RuntimeException{
    public CheckInAlreadyExistsException(String message) {
        super(message);
    }
}
