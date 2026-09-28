package com.studentmanagement.entity;

import com.studentMangement.entity.Student;
import com.studentMangement.entity.StudentStatus;
import com.studentMangement.exception.InvalidStudentException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class StudentTest {
@Test
void testStudentId(){
    Student student = new Student(
            1,
            "Griffins Kyei",
            "griffinskyei6@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2026,1,10),
                StudentStatus.ACTIVE
    );
assertEquals(1, student.getId());
}
@Test
    void shouldRejectInvalidId(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    0,
                    "Griffins Kyei",
                    "griffinskyei6@gmail.com",
                    20,
                    3.5,
                    "Computer Science",
                    LocalDate.of(2026,1,10),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectBlankName(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    1,
                    " ",
                    "griffinskyei6.gmail.com",
                    20,
                    3.5,
                    "Computer Science",
                    LocalDate.of(2026,1,10),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectInvalidEmail(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    1,
                    "Griffins Musyoki",
                    "griffinsmusyoki.com",
                    20,
                    3.5,
                    "Information Technology",
                    LocalDate.of(2026,1,10),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectInvalidAge(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    3,
                    "Griffins Derrick",
                    "griffins@gmail.com",
                    15,
                    3.5,
                    "Business information Technology",
                    LocalDate.of(2026,1,10),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectInvalidGpa(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    3,
                    "James Wainyari",
                    "james@gmail.com",
                    20,
                    4.5,
                    "Data Science",
                    LocalDate.of(2026,1,10),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectNullMajor(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    5,
                    "Mary Gibson",
                    "mary@gmail.com",
                    24,
                    4.0,
                    null,
                    LocalDate.of(2026,1,10),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectFutureEnrollmentDate(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    6,
                    "John Mathew",
                    "john@gmail.com",
                    20,
                    3.0,
                    "Statistics",
                    LocalDate.now().plusDays(1),
                    StudentStatus.ACTIVE
            )
    );
}
@Test
    void shouldRejectNullStatus(){
    assertThrows(
            InvalidStudentException.class,
            () -> new Student(
                    7,
                    "Dennis Kimathi",
                    "dennis@gmail.com",
                    20,
                    4.0,
                    "Business information Technology",
                    LocalDate.of(2025,8,25),
                   null
            )
    );
}
@Test
    void shouldCompareStudentsByGpaDescending(){
    Student student1 = new Student(
            8,
            "Stephen Odero",
            "stephen@gmail.com",
            21,
            3.8,
            "Information Technology",
            LocalDate.of(2025,8,25),
            StudentStatus.ACTIVE
    );
    Student student2 = new Student(
            2,
            "Marry Jane",
            "marry@gmail.com",
            17,
            3.4,
            "Mechatronics",
            LocalDate.of(2025,8,25),
            StudentStatus.ACTIVE
    );
    assertTrue(student1.compareTo(student2) <0);
}
@Test
    void shouldCompareStudentsByNameWhenGpaIsEqual(){
    Student student1 = new Student(
            1,
            "Alice Tracy",
            "alice@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2025,8,25),
            StudentStatus.ACTIVE
    );
    Student student2 = new Student(
            2,
            "Bob Ali",
            "bob@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2025,8,25),
            StudentStatus.ACTIVE
    );
    assertTrue(student1.compareTo(student2)<0);
}
@Test
void shouldCompareWithStudentByIdWhenGpaAndNameAreEqual(){
    Student student1 = new Student(
            1,
            "Alice Tracy",
            "alice1@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2026,8,25),
            StudentStatus.ACTIVE
    );
    Student student2 = new Student(
            2,
            "Alice Tracy",
            "alice2@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2026,8,25),
            StudentStatus.ACTIVE
    );
    assertTrue(student1.compareTo(student2)<1);
}
@Test
    void shouldReturnZeroWhenStudentAreEqual(){
    Student student1 = new Student(
            1,
            "Bob Ali",
            "bob@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2026,8,25),
            StudentStatus.ACTIVE
    );
    Student student2 = new Student(
            1,
            "Bob Ali",
            "bob@gmail.com",
            20,
            3.5,
            "Computer Science",
            LocalDate.of(2026,8,25),
            StudentStatus.ACTIVE
    );
    assertEquals(0,student1.compareTo(student2));
}
@Test
    void shouldRejectInvalidGpaUpdate(){
    Student student = new Student(
            1,
            "Bob Ali",
            "bob@gmail.com",
             20,
            3.5,
            "Computer Science",
            LocalDate.of(2026,8,25),
            StudentStatus.ACTIVE
    );
    assertThrows(
            InvalidStudentException.class,
            () ->student.setGpa(5.0)
    );
}
}
