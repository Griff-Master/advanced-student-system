package com.studentmanagement.exception;

public class InvalidStatusTransitionException extends RuntimeException{
    public InvalidStatusTransitionException(String message){
        super(message);
    }
}
