package com.studentMangement.repository;

import com.studentMangement.entity.Student;
import com.studentMangement.exception.InvalidStudentException;
import java.util.*;

public class InMemoryStudentRepository implements Repository<Student,Integer>{
private final Map<Integer,Student>students = new HashMap<>();
     @Override
    public Student save(Student student) {
        if(student == null){
            throw new InvalidStudentException(
                    "Student cannot be null."
            );
        }
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public List<Student> saveAll(Collection<Student> students) {
        if(students == null){
            throw new InvalidStudentException(
                    "Students collection cannot be null."
            );
        }
        List<Student>saved = new ArrayList<>();
        for(Student student : students){
            if(student == null){
                throw new InvalidStudentException(
                        "Student cannot be null."
                );
            }

        }
        for(Student student : students) {

            saved.add(save(student));
        }
        return saved;
    }

    @Override
    public Optional<Student> findById(Integer id) {
         return Optional.ofNullable(students.get(id));
    }

    @Override
    public List<Student> findAll() {
         return new ArrayList<>(students.values());
    }

    @Override
    public boolean deleteById(Integer id) {
         return students.remove(id) !=null;
    }

    @Override
    public boolean existsById(Integer id) {
         return students.containsKey(id);
    }

    @Override
    public long count() {
        return students.size();
    }
}
