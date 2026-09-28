package com.studentMangement.entity;

import com.studentMangement.exception.InvalidStudentException;
import java.time.LocalDate;

public class Student implements Comparable<Student> {
    private final int id;
    private String name;
    private String email;
    private int age;
    private double gpa;
    private String major;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    public Student(
            int id,
            String name,
            String email,
            int age,
            double gpa,
            String major,
            LocalDate enrollmentDate,
            StudentStatus status
    ){
        if(id <= 0){
            throw new InvalidStudentException("ID must be positive.");
        }
        this.id = id;
        setName(name);
        setEmail(email);
        setAge(age);
        setGpa(gpa);
         setMajor(major);
         setEnrollmentDate(enrollmentDate);
        setStatus(status);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public double getGpa() {
        return gpa;
    }
    public String getMajor(){
        return major;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        if(name == null){
            throw new InvalidStudentException(
                    "Name is required."
            );
        }
        String trimmedName = name.trim();
        if(trimmedName.isBlank()
                || trimmedName.length()<2
                || trimmedName.length()>100){
            throw new InvalidStudentException(
                    "Name must be between 2 and 100 characters."
            );
        }
        this.name = trimmedName;
    }

    public void setEmail(String email) {
        if(email == null){
            throw new InvalidStudentException(
                    "Email is required."
            );
        }
        String trimmedEmail = email.trim();
        if(trimmedEmail.isBlank()){
            throw new InvalidStudentException(
                    "Email is required."
            );
        }
        String emailRegex = "^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if(!trimmedEmail.matches(emailRegex)){
            throw new InvalidStudentException(
                    "Invalid email format."
            );
        }
        this.email = trimmedEmail;
    }

    public void setGpa(double gpa) {
        if(gpa <0.0 || gpa > 4.0){
            throw new InvalidStudentException(
                    "Gpa must be between 0 and 4."
            );
        }
        this.gpa = gpa;
    }

    public void setMajor(String major) {
        if(major == null){
            throw new InvalidStudentException(
                    "Major is required."
            );
        }
        String trimmedMajor = major.trim();
        if(trimmedMajor.isBlank()
        ||trimmedMajor.length() <2
        || trimmedMajor.length() >100){
            throw new InvalidStudentException(
                    "Major must be between 2 and 100 characters."
            );
        }
        this.major = trimmedMajor;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        if(enrollmentDate == null){
            throw new InvalidStudentException(
                    "EnrollmentDate is required."
            );
        }
        if(enrollmentDate.isAfter(LocalDate.now())){
            throw new InvalidStudentException(
                    "EnrollmentDate cannot be in the future."
            );
        }
        this.enrollmentDate = enrollmentDate;
    }

    public void setAge(int age) {
        if(age <16 || age > 60){
            throw new InvalidStudentException(
                    "Age must be between 16 and 60."
            );
        }
        this.age = age;
    }

    public void setStatus(StudentStatus status) {
        if(status == null){
            throw new InvalidStudentException(
                    "Student status is required."
            );
        }
        this.status = status;
    }
    @Override
    public int compareTo(Student other){
        int gpaComparison = Double.compare(other.gpa, this.gpa);
        if(gpaComparison != 0){
            return gpaComparison;
        }
        int nameComparison = this.name.compareToIgnoreCase(other.name);
        if(nameComparison !=0 ){
            return nameComparison;
        }
        return Integer.compare(this.id, other.id);
    }
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Student student)){
            return false;
        }
        return this.id == student.id;
    }
    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
    @Override
    public String toString(){
        return "\nStudent ID      : " + id +
               "\nStudent name    : " + name +
                "\nStudent email  : " + email +
                "\nStudent Age    : " + age   +
                "\nStudent gpa    : " + gpa +
                "\nMajor          : " + major +
                "\nEnrollment Date: " + enrollmentDate +
                "\nStudent Status : " + status;


    }
}

