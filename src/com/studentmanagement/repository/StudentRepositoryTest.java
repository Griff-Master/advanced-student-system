package com.studentmanagement.repository;

import com.studentMangement.entity.Student;
import com.studentMangement.entity.StudentStatus;
import com.studentMangement.repository.JsonStudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudentRepositoryTest {
@TempDir
Path tempDir;
private Student createStudent(){
    return new Student(
            1,
            "John Mark",
            "john@gmail.com",
            20,
            3.7,
            "Computer Science",
            LocalDate.of(2026,8,31),
            StudentStatus.ACTIVE
    );
}
@Test
    void shouldSaveAndLoadStudentUsingJson(){
    Path file = tempDir.resolve("students.json");
    JsonStudentRepository repository =
            new JsonStudentRepository(file);
    Student student = createStudent();
    repository.save(student);
    JsonStudentRepository loadedRepository =
            new JsonStudentRepository(file);
    var result = loadedRepository.findById(1);
    assertTrue(result.isPresent());
    assertEquals(student,result.get());
}
    }

