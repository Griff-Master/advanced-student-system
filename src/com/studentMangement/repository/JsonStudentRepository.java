package com.studentMangement.repository;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.studentMangement.entity.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.studentMangement.exception.InvalidStudentException;
import com.studentMangement.exception.StudentPersistenceException;
import com.studentMangement.util.LocalDateTypeAdapter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

public class JsonStudentRepository implements Repository<Student,Integer> {
    private final Path filePath;
    private final Gson gson;
    private final Map<Integer,Student> students;
    //default
    public JsonStudentRepository(){
        this(Path.of("students.json"));
    }
    //Configurable
    public JsonStudentRepository(Path filePath){
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        new LocalDateTypeAdapter()
                )
                .setPrettyPrinting()
                .create();
        this.students = new HashMap<>();
        try{
            Path parent = filePath.getParent();
            if(parent !=null){
                Files.createDirectories(parent);
            }
            if(Files.notExists(filePath)){
                Files.createFile(filePath);
            }
           loadFromFile();
        }catch (IOException e){
            throw new StudentPersistenceException(
                    "Failed to prepare student data file." , e
            );

        }
    }
    private void loadFromFile(){
        try{
            if(Files.size(filePath) == 0){
                return;
            }
            try(Reader reader = Files.newBufferedReader(
                    filePath,
                StandardCharsets.UTF_8
            )){
                Type type = new TypeToken<Map<Integer,Student>>(){}.getType();
                Map<Integer,Student>loaded =
                        gson.fromJson(reader, type);
                if(loaded != null){
                    students.putAll(loaded);
                }
            }
        }catch(IOException | JsonSyntaxException | JsonIOException e){
            throw new StudentPersistenceException(
                    "Failed to load students from Json file" ,
                    e
            );
        }
    }
    private void persistToFile() {
        Path tempFile = filePath.resolveSibling(
                filePath.getFileName() + ".tmp"
        );
        try {
            try (Writer writer = Files.newBufferedWriter(
                    tempFile,
                    StandardCharsets.UTF_8
            )) {
                gson.toJson(students, writer);
            }
            try {
                Files.move(
                        tempFile,
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {

                    Files.move(
                            tempFile,
                            filePath,
                            StandardCopyOption.REPLACE_EXISTING
                    );

                }

            }catch (IOException | JsonIOException e) {
            throw new StudentPersistenceException(
                    "Failed to save students to Json file.",
                    e
            );

        }
    }
    private void saveToMemory(Student student) {
        students.put(student.getId(), student);
    }
    @Override
    public Student save(Student student){
        if(student ==null){
            throw new InvalidStudentException(
                    "Student cannot be null."
            );
        }
       saveToMemory(student);
        persistToFile();
        return student;
    }
    @Override
    public List<Student>saveAll (Collection<Student> studentsToSave) {
        if (studentsToSave == null) {
            throw new InvalidStudentException(
                    "Student collection cannot be null."
            );
        }
        List<Student>saved = new ArrayList<>();
        for (Student student : studentsToSave) {
            if (student == null) {
                throw new InvalidStudentException(
                        "Student collection cannot contain a null entry."
                );
            }
        }
        for(Student student: studentsToSave) {
            saveToMemory(student);
saved.add(student);

        }

        persistToFile();
        return saved;
    }
    @Override
    public boolean deleteById( Integer id){
        boolean removed = students.remove(id) != null;
        if(removed){
            persistToFile();
        }
        return removed;
    }
    @Override
    public Optional<Student>findById(Integer id){
        return Optional.ofNullable(students.get(id));
    }
    @Override
    public List<Student>findAll(){
        return new ArrayList<>(students.values());
    }
    @Override
    public boolean existsById(Integer id){
        return students.containsKey(id);
    }
    @Override
    public long count(){
        return students.size();
    }
}
