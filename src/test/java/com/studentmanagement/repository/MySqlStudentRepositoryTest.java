package com.studentmanagement.repository;

import com.studentmanagement.config.DatabaseConnection;
import com.studentmanagement.entity.Student;
import com.studentmanagement.entity.StudentStatus;
import com.studentmanagement.exception.StudentPersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySqlStudentRepositoryTest {
    private MySqlStudentRepository repository;
    private Student student;
    private Student secondStudent;

    @BeforeEach
    void setUp() {
        repository = new MySqlStudentRepository();

        student = new Student(
                101,
                "Test Student",
                "test.student101@example.com",
                20,
                3.5,
                "BBIT",
                LocalDate.of(2025, 8, 25),
                StudentStatus.ACTIVE
        );
        secondStudent = new Student(
                102,
                "Second Test Student",
                "test.student102@example.com",
                21,
                3.7,
                "Computer Science",
                LocalDate.of(2025, 9, 10),
                StudentStatus.ACTIVE
        );
    }

    @Test
    void shouldSaveStudent() {
        Student savedStudent = repository.save(student);
        assertEquals(student.getId(), savedStudent.getId());
        var result = repository.findById(student.getId());
        assertTrue(result.isPresent());
        assertEquals(student.getEmail(),result.get().getEmail());
    }

    @AfterEach
    void cleanUp() throws SQLException {
        String sql = "DELETE FROM students WHERE id in(?,?,?) ";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, student.getId());
            statement.setInt(2, secondStudent.getId());
            statement.setInt(3,103);
            statement.executeUpdate();
        }
    }
@Test
    void shouldFindStudentById(){
        repository.save(student);
                var result = repository.findById(student.getId());
        assertTrue( result.isPresent());
        assertEquals(student.getEmail(), result.get().getEmail());
}
    @Test
    void shouldReturnEmptyWhenStudentDoesNotExist() {
        var emptyResult = repository.findById(999);
        assertTrue(emptyResult.isEmpty());

    }
@Test
    void shouldReturnTrueWhenStudentExists(){
        repository.save(student);
        assertTrue(repository.existsById(student.getId()));
}
    @Test
    void shouldReturnFalseWhenStudentDoesNotExist() {
        assertFalse(repository.existsById(999));
    }
@Test
    void shouldDeleteStudentById(){
        repository.save(student);
        boolean deleted = repository.deleteById(student.getId());
        assertTrue(deleted);
        assertFalse(repository.findById(student.getId()).isPresent());

}
    @Test
    void shouldReturnFalseWhenDeletingNonexistentStudent() {
        assertFalse(repository.deleteById(999));
    }
    @Test
    void shouldFindAllStudents() {
        repository.save(student);


        var students = repository.findAll();

        assertFalse(students.isEmpty());
        assertTrue(
                students.stream()
                        .anyMatch(s -> s.getEmail().equals(student.getEmail()))
        );


    }
    @Test
    void shouldCountStudents() {
        long countBefore = repository.count();


        repository.save(student);

        long countAfter = repository.count();

        assertEquals(countBefore + 1, countAfter);


    }
    @Test
    void shouldSaveAllStudents() {
        var savedStudents = repository.saveAll(
                List.of(student, secondStudent)
        );

        assertEquals(2, savedStudents.size());

        assertTrue(repository.findById(101).isPresent());
        assertTrue(repository.findById(102).isPresent());

    }
    @Test
    void shouldRollbackWhenSaveAllFails() {
        Student duplicateEmailStudent = new Student(
                103,
                "Duplicate Email Student",
                student.getEmail(),
                22,
                3.2,
                "BBIT",
                LocalDate.of(2025, 9, 15),
                StudentStatus.ACTIVE
        );
        assertThrows(StudentPersistenceException.class,
                () -> repository.saveAll(List.of(student,duplicateEmailStudent))
        );
        var result1 = repository.findById(student.getId());
        var result2 = repository.findById(duplicateEmailStudent.getId());
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());

    }
    @Test
    void shouldUseTestDatabase() throws SQLException {
        try (var connection = DatabaseConnection.getConnection()) {
            assertEquals(
                    "student_management_test",
                    connection.getCatalog()
            );
        }
    }
}
