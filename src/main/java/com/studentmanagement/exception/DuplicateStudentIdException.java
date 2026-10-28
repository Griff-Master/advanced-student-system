package com.studentmanagement.exception;

public class DuplicateStudentIdException extends RuntimeException {
      public DuplicateStudentIdException(String message){
          super(message);
      }
}
