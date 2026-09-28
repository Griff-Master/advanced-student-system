package com.studentMangement.util;

import com.studentMangement.entity.Student;

import java.util.Comparator;

public final class StudentComparators {
    private StudentComparators(){
        // prevent creating objects of this class
    }
    public static final Comparator<Student>BY_NAME =
            Comparator.comparing(
                    Student::getName,
                    String.CASE_INSENSITIVE_ORDER
            );
    public static final Comparator<Student>BY_ENROLLMENT_DATE =
            Comparator.comparing(Student::getEnrollmentDate);
    public static final Comparator<Student>BY_MAJOR =
            Comparator.comparing(
                    Student::getMajor,
                    String.CASE_INSENSITIVE_ORDER
            );
    public static final Comparator<Student>BY_STATUS =
            Comparator.comparing(Student::getStatus);
}
