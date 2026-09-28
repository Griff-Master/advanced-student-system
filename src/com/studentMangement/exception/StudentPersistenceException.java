package com.studentMangement.exception;


public class StudentPersistenceException extends RuntimeException{
        public StudentPersistenceException(String message, Throwable cause){
            super(message, cause);
        }
    }

