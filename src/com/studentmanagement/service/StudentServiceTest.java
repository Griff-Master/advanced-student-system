package com.studentmanagement.service;

import com.studentMangement.entity.Student;
import com.studentMangement.entity.StudentStatus;
import com.studentMangement.exception.DuplicateEmailException;
import com.studentMangement.exception.DuplicateStudentIdException;
import com.studentMangement.exception.InvalidStatusTransitionException;
import com.studentMangement.exception.StudentNotFoundException;
import com.studentMangement.repository.InMemoryStudentRepository;
import com.studentMangement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {
    private InMemoryStudentRepository repository;
    private StudentService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryStudentRepository();
        service = new StudentService(repository);
    }

    @Test
    void ShouldRegisterStudent() {
        Student student = new Student(
                1,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                3.5,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student savedStudent = service.registerStudent(student);
        assertEquals(student, savedStudent);
        assertTrue(repository.existsById(1));
    }

    @Test
    void shouldRejectDuplicateStudentId() {
        Student student1 = new Student(
                1,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                3.5,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                1,
                "Alice Jane",
                "alice@gmail.com",
                19,
                3.6,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE

        );
        service.registerStudent(student1);
        assertThrows(
                DuplicateStudentIdException.class,
                () -> service.registerStudent(student2)
        );
    }

    @Test
    void shouldRejectDuplicateEmailIgnoringCase() {
        Student student1 = new Student(
                1,
                "Mary Nancy",
                "mary@gmail.com",
                17,
                3.7,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                2,
                "Mary Jane",
                "MARY@gmail.com",
                18,
                3.8,
                "Computer Science",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        assertThrows(
                DuplicateEmailException.class,
                () -> service.registerStudent(student2)
        );
    }

    @Test
    void shouldThrowWhenStudentIsNotFound() {
        assertThrows(
                StudentNotFoundException.class,
                () -> service.findById(9999)
        );
    }

    @Test
    void shouldUpdateStudentGpa() {
        Student student = new Student(
                1,
                "John Mark",
                "john@gmail.com",
                20,
                3.4,
                "Mechatronics",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student);
        Student updatedStudent = service.updateStudentGpa(1, 3.8);
        assertEquals(3.8, updatedStudent.getGpa());
    }

    @Test
    void shouldChangeStudentStatus() {
        Student student = new Student(
                1,
                "John Mark",
                "john@gmail.com",
                20,
                3.0,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student);
        Student updatedStudent = service.changeStudentStatus(1, StudentStatus.SUSPENDED);
        assertEquals(StudentStatus.SUSPENDED, updatedStudent.getStatus());
    }

    @Test
    void shouldRejectInvalidStatusTransitions() {
        Student student = new Student(
                1,
                "John Paul",
                "john@gmail.com",
                20,
                3.9,
                "Electrical Engineering",
                LocalDate.of(2025, 8, 25),
                StudentStatus.GRADUATED
        );
        service.registerStudent(student);
        assertThrows(
                InvalidStatusTransitionException.class,
                () -> service.changeStudentStatus(1, StudentStatus.ACTIVE)
        );
    }

    @Test
    void shouldGraduateStudent() {
        Student student = new Student(
                1,
                "John Paul",
                "john@gmail.com",
                20,
                4.0,
                "Mechatronics",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student);
        Student graduatedStudent = service.graduateStudent(1);
        assertEquals(StudentStatus.GRADUATED, graduatedStudent.getStatus());
    }

    @Test
    void shouldSearchStudentByName() {
        Student student1 = new Student(
                1,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                4.0,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                2,
                "Alice cheplegat",
                "alice@gmail.com",
                17,
                3.7,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        service.registerStudent(student2);
        var results = service.searchByName("griffins");
        assertEquals(1, results.size());
        assertEquals("Griffins Jackson", results.getFirst().getName());
    }

    @Test
    void shouldFindStudentByMajor() {
        Student student1 = new Student(
                1,
                "John Mark",
                "john@gmail.com",
                20,
                3.8,
                "Computer Science",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                2,
                "James Gibson",
                "james@gmail.com",
                20,
                4.0,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        service.registerStudent(student2);
        var results = service.findByMajor("Computer science");
        assertEquals(1, results.size());
        assertEquals("John Mark", results.getFirst().getName());

    }

    @Test
    void shouldReturnTopStudents() {
        Student student1 = new Student(
                1,
                "Mary Lindsey",
                "mary@gmail.com",
                20,
                4.0,
                "Computer Science",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                2,
                "John Michael",
                "john@gmail.com",
                19,
                3.4,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student3 = new Student(
                3,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                3.9,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        service.registerStudent(student2);
        service.registerStudent(student3);
        var results = service.getTopStudents(2);
        assertEquals(2, results.size());
        assertEquals("Mary Lindsey", results.getFirst().getName());
        assertEquals("Griffins Jackson", results.get(1).getName());

    }

    @Test
    void shouldFindStudentWithGpaAtLeastThreshold() {
        Student student1 = new Student(
                1,
                "Mary Lindsey",
                "mary@gmail.com",
                19,
                3.5,
                "Computer Science",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                2,
                "John Michael",
                "john@gmail.com",
                20,
                3.4,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student3 = new Student(
                3,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                4.0,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        service.registerStudent(student2);
        service.registerStudent(student3);
        var results = service.findStudentsWithGpaAtLeast(3.5);
        assertEquals(2, results.size());
        assertEquals("Griffins Jackson", results.getFirst().getName());
        assertEquals("Mary Lindsey", results.get(1).getName());
    }

    @Test
    void shouldReturnStudentsByPage() {
        Student student1 = new Student(
                1,
                "Mary Lindsey",
                "mary@gmail.com",
                19,
                3.9,
                "Computer Science",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student2 = new Student(
                2,
                "John Michael",
                "john@gmail.com",
                20,
                3.7,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        Student student3 = new Student(
                3,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                4.0,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        service.registerStudent(student2);
        service.registerStudent(student3);
        var results = service.findAll(0, 2);
        assertEquals(2, results.size());
        assertEquals("Griffins Jackson", results.getFirst().getName());
        assertEquals("Mary Lindsey", results.get(1).getName());
    }

    @Test
    void shouldCountStudentsByStatus() {
        Student student1 = new Student(
                1,
                "Jane Lindsey",
                "jane@gmail.com",
                19,
                3.9,
                "Computer Science",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );

        Student student2 = new Student(
                2,
                "John Michael",
                "john@gmail.com",
                20,
                3.7,
                "Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.SUSPENDED
        );
        Student student3 = new Student(
                3,
                "Griffins Jackson",
                "griffins@gmail.com",
                20,
                4.0,
                "Business Information Technology",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        service.registerStudent(student1);
        service.registerStudent(student2);
        service.registerStudent(student3);
        var count = service.countByStatus();
        assertEquals(2, count.get(StudentStatus.ACTIVE));
        assertEquals(1,count.get(StudentStatus.SUSPENDED));
    }
}