package com.studentmanagement.exception;

public class InvalidStudentException extends  RuntimeException{
    public InvalidStudentException(String message){
        super(message);
    }
}
