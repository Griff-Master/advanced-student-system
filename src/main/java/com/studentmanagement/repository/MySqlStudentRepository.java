package com.studentmanagement.repository;
import com.studentmanagement.config.DatabaseConnection;
import com.studentmanagement.entity.Student;
import com.studentmanagement.entity.StudentStatus;
import com.studentmanagement.exception.InvalidStudentException;
import com.studentmanagement.exception.StudentPersistenceException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import  java.sql.Date;

public class MySqlStudentRepository implements Repository<Student,Integer>{
    @Override
    public Student save(Student student) {
        if(student == null){
            throw new InvalidStudentException(
                    "Student cannot be null."
            );
        }
        String sql = """
                insert into students
                (id,name,email,age,gpa,major,enrollment_date,status)
                values(?,?,?,?,?,?,?,?)
                """;
        try(
                var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(sql)
                ){
            statement.setInt(1,student.getId());
            statement.setString(2,student.getName());
            statement.setString(3,student.getEmail());
            statement.setInt(4,student.getAge());
            statement.setDouble(5,student.getGpa());
            statement.setString(6,student.getMajor());
            statement.setDate(
                    7,
                    Date.valueOf(student.getEnrollmentDate())
            );
            statement.setString(8,student.getStatus().name());
            statement.executeUpdate();
            return student;
        }catch (SQLException e){
            throw new StudentPersistenceException(
                    "Failed to save Student.", e
            );
        }
    }

    @Override
    public List<Student> saveAll(Collection<Student> students) {
      if(students == null){
          throw new InvalidStudentException(
                  "Students collection cannot be null."
          );
      }
      if(students.isEmpty()){
          throw new InvalidStudentException(
                  "Students collection cannot be empty."
          );
      }
      for(Student student : students){
          if (student == null){
              throw new InvalidStudentException(
                      "Students collection cannot contain null."
              );
          }
      }
      String sql = """
              insert into students
              (id,name,email,age,gpa,major,enrollment_date,status)
              values(?,?,?,?,?,?,?,?)
              """;
      try(var connection = DatabaseConnection.getConnection()){
          connection.setAutoCommit(false);
          try(var statement = connection.prepareStatement(sql)){
              for(Student student : students){
                  statement.setInt(1,student.getId());
                  statement.setString(2,student.getName());
                  statement.setString(3,student.getEmail());
                  statement.setInt(4,student.getAge());
                  statement.setDouble(5,student.getGpa());
                  statement.setString(6, student.getMajor());
                  statement.setDate(
                          7,
                          Date.valueOf(student.getEnrollmentDate())
                  );
                  statement.setString(8,student.getStatus().name());
                  statement.addBatch();
              }
              statement.executeBatch();
              connection.commit();
              return new ArrayList<>(students);
          }catch (SQLException e){
              try{
                  connection.rollback();
              }catch (SQLException rollbackException){
                  e.addSuppressed(rollbackException);
              }
              throw new StudentPersistenceException(
                      "Failed to save students.", e
              );
          }
      }catch(SQLException e){
          throw new StudentPersistenceException(
                  "Failed to save students.", e
          );
      }
    }

    @Override
    public Optional<Student> findById(Integer id) {
        String sql = """
                select id, name, email, age, gpa, major,enrollment_date, status
                from students
                where id = ?
                """;
        try(
                var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(sql)
                ){
            statement.setInt(1,id);

            try(var resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Student student = new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getInt("age"),
                            resultSet.getDouble("gpa"),
                            resultSet.getString("major"),
                            resultSet.getDate("enrollment_date").toLocalDate(),
                            StudentStatus.valueOf(resultSet.getString("status"))
                    );
                    return  Optional.of(student);
                }
                return  Optional.empty();
            }
        }catch (SQLException e){
            throw new StudentPersistenceException(
                    "Failed to find student by ID.", e
            );
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student>students = new ArrayList<>();
        String sql = """
                select id, name,email,age, gpa, major, enrollment_date,status
                from students
                """;
        try(
                var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(sql);
                var resultSet = statement.executeQuery()
                ){
            while(resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age"),
                        resultSet.getDouble("gpa"),
                        resultSet.getString("major"),
                        resultSet.getDate("enrollment_date").toLocalDate(),
                        StudentStatus.valueOf(resultSet.getString("status"))
                );
                students.add(student);
            }
            return students;
        }catch(SQLException e){
            throw new StudentPersistenceException(
                    "Failed to retrieve all students.", e
            );
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "delete from students where id = ?";
        try(
                var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(sql)
                ){
            statement.setInt(1,id);
            int rowsAffected = statement.executeUpdate();
            return  rowsAffected > 0;

        }catch (SQLException e){
            throw new StudentPersistenceException(
                    "Failed to delete student.", e
            );
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = """
                select id from students
                where id = ?
                """;
        try(var connection = DatabaseConnection.getConnection();
            var statement = connection.prepareStatement(sql)
        ){
         statement.setInt(1,id);
         try(var resultSet = statement.executeQuery()){
             return resultSet.next();
         }
        }catch (SQLException e){
            throw new StudentPersistenceException(
                    "Failed to check if student exists.",e
            );
        }
    }

    @Override
    public long count() {
        String sql = "select count(*) from students";
        try(
                var connection = DatabaseConnection.getConnection();
                var statement = connection.prepareStatement(sql);
                var resultSet = statement.executeQuery()
                ){
               resultSet.next();
               return resultSet.getLong(1);
        }catch (SQLException e){
            throw new StudentPersistenceException(
                    "Failed to count students.",e
            );
        }
    }
}
