package com.studentMangement.service;

import  com.studentMangement.entity.Student;
import com.studentMangement.entity.StudentStatus;
import com.studentMangement.exception.*;
import com.studentMangement.repository.Repository;
import com.studentMangement.util.StudentComparators;
import java.util.*;
import java.util.stream.Collectors;


public class StudentService {
    private final Repository<Student,Integer >repository;

    public StudentService(
            Repository<Student,Integer>repository){
        this.repository = repository;

    }
    public Student registerStudent(Student student){
        if(repository.existsById(student.getId())){
            throw new DuplicateStudentIdException(
                    "Student ID already exists."
            );
        }
        boolean emailExists = repository.findAll()
                .stream()
                .anyMatch(existingStudent ->
                        existingStudent.getEmail()
                                .equalsIgnoreCase(student.getEmail()));
        if(emailExists){
            throw new DuplicateEmailException("Student email already exists.");
        }
        return repository.save(student);
    }
    public List<Student>registerStudents(List<Student>students){
        if(students == null){
            throw new InvalidStudentException(
                    "Student collection cannot be null."
            );
        }
        if(students.isEmpty()){
            throw new InvalidStudentException(
                    "Student collection cannot be empty."
            );
        }
        Set<Integer>ids = new HashSet<>();
        Set<String>emails = new HashSet<>();
        List<Student>existingStudents = repository.findAll();
        for(Student student : students){
            if(student == null){
                throw new InvalidStudentException(
                        "Student collection cannot contain null."
                );
            }
            if(!ids.add(student.getId())){
                throw new DuplicateStudentIdException(
                        "Duplicate student ID in bulk collection: " +
                        student.getId()
                );
            }
            String email = student.getEmail().toLowerCase();
            if(!emails.add(email)){
               throw new DuplicateEmailException(
                       "Duplicate email in bulk collection: "
                       + student.getEmail()
               );
            }
        }
        for(Student student : students){
            if(repository.existsById(student.getId())){
                throw new DuplicateStudentIdException(
                        "Student ID already exists: "
                        + student.getId()
                );
            }
            boolean emailExists = existingStudents.stream()
                    .anyMatch(existing ->
                            existing.getEmail()
                                    .equalsIgnoreCase(
                                            student.getEmail()
                                    ));
            if(emailExists){
                throw new DuplicateEmailException(
                        "Student email already exists: "
                        + student.getEmail()
                );
            }
        }
        return repository.saveAll(students);
    }
    public  Student updateStudentGpa(int id, double newGpa){
Student student = findById(id);
student.setGpa(newGpa);
        return repository.save(student);
    }
    public Student changeStudentStatus(int id, StudentStatus status){
        if(status == null){
            throw new InvalidStudentException("Status is required.");
        }
        Student student =findById(id);
        StudentStatus currentStatus = student.getStatus();

        // same status is allowed as no-op
        if(currentStatus == status){
            return student;
        }
        switch(currentStatus){
            case ACTIVE:
                if(status != StudentStatus.GRADUATED
                && status != StudentStatus.SUSPENDED
                        && status != StudentStatus.DROPPED){
                    throw new InvalidStatusTransitionException(
                            "Active cannot change to " + status
                    );
                }
                break;
            case SUSPENDED:
                if(status != StudentStatus.ACTIVE
                && status != StudentStatus.DROPPED){
                    throw new InvalidStatusTransitionException(
                            "Suspended cannot change to " + status
                    );
                }
                break;
            case  GRADUATED:
            case DROPPED:
                throw new InvalidStatusTransitionException(
                        currentStatus + " is a terminal status."
                );
        }
        student.setStatus(status);
        return repository.save(student);
    }
    public Student graduateStudent(int id){

        return changeStudentStatus(id,StudentStatus.GRADUATED);
    }
    public Student findById(int id){
        return repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student of ID " + id + " not found."));
    }
    public List<Student> searchByName(String query){
        if(query == null || query.isBlank()){
            throw  new InvalidStudentException("Search query cannot be null or blank.");
        }
        String search = query.trim().toLowerCase()
                .replaceAll("\\s+", " ");
   return repository.findAll()
                .stream()
                .filter(student ->
                        student.getName().toLowerCase()
                                .replaceAll("\\s+", " ")
                                .contains(search))
                .toList();
    }
    public List<Student>findByMajor(String major){
        if(major == null || major.isBlank()){
           throw new InvalidStudentException(
                   "Major cannot be null or blank."
           );
        }
        String searchMajor = major.trim()
                .replaceAll("\\s+", " ");
        return repository.findAll()
                .stream()
                .filter(student ->
                        student.getMajor()
                                .replaceAll("\\s+", " ")
                                .equalsIgnoreCase(searchMajor))
                .toList();

    }
    public List<Student>getTopStudents(int limit){
        if(limit <=0){
            throw new InvalidStudentException(
                    "Limit must be greater than zero."
            );
        }
        return repository.findAll()
                .stream()
                .sorted()
                .limit(limit)
                .toList();
    }
    public List<Student> findStudentsWithGpaAtLeast(double threshold){
        if(threshold < 0.0 || threshold > 4.0){
            throw  new InvalidStudentException("Valid Gpa required.");
        }
        return repository.findAll()
                .stream()
                .sorted()
                .filter(student ->student.getGpa() >= threshold)
                .toList();
    }
    public List<Student> findAll(int page, int size){
        if(page < 0){
            throw new InvalidStudentException(
                    "Page cannot be negative."
            );
        }
        if(size <= 0){
            throw new InvalidStudentException(
                    "Size must be greater than zero."
            );
        }
        long offset = (long)page *size;
        return repository.findAll()
                .stream()
                .sorted()
                .skip(offset)
                .limit(size)
                .toList();
    }
    public Map<StudentStatus, Long> countByStatus(){
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getStatus,
                        Collectors.counting()
                ));
    }
    public List<Student>sortStudents(int sortOption){
        Comparator<Student> comparator;
        switch (sortOption){
            case 1 -> comparator = StudentComparators.BY_NAME;
            case 2 -> comparator = StudentComparators.BY_ENROLLMENT_DATE;
            case 3 -> comparator = StudentComparators.BY_MAJOR;
            case 4 -> comparator = StudentComparators.BY_STATUS;
            default -> throw new InvalidStudentException(
                    "Invalid sorting option."
            );
        }
        return repository.findAll()
                .stream()
                .sorted(comparator)
                .toList();
    }
}
