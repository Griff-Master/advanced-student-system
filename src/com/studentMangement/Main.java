package com.studentMangement;

import com.studentMangement.entity.Student;
import com.studentMangement.entity.StudentStatus;
import com.studentMangement.exception.*;
import com.studentMangement.repository.JsonStudentRepository;
import com.studentMangement.repository.Repository;
import com.studentMangement.service.StudentService;
import com.studentMangement.util.ConsoleUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {
    public void main(String[] args) {
        Repository<Student, Integer> repository =
                new JsonStudentRepository();
        StudentService service =
                new StudentService(repository);
        ConsoleUtils console =
                new ConsoleUtils();
        boolean running = true;

        console.showMessage("==================================");
        console.showMessage("   STUDENT MANAGEMENT SYSTEM");
        console.showMessage("==================================");

        while (running) {
            displayMainMenu();
            int choice = console.readMenuChoice(1, 13);
            try {
                switch (choice) {
                    case 1 -> addingSingleStudent(console, service);

                    case 2 -> addStudentsInBulk(console, service);
                    case 3 -> viewStudents(console, service);
                    case 4 -> findStudentByID(console, service);
                    case 5 -> searchStudentsByName(console, service);
                    case 6 -> viewStudentsByMajor(console, service);
                    case 7 -> updateStudentGPA(console, service);
                    case 8 -> changeStudentStatus(console, service);
                    case 9 -> graduateStudent(console, service);
                    case 10 -> showTopStudents(console, service);
                    case 11 -> showSummaryByStatus(console, service);
                    case 12 -> sortStudents(console, service);
                    case 13 -> {
                        console.showSuccess(
                                "If made any changes they " +
                                        "have already been persisted."
                        );
                        console.showMessage(
                                "Exiting Student Management System..."
                        );
                        running = false;
                    }
                }
            } catch (StudentPersistenceException e) {
                console.showError(
                        "Unable to save or load student data: "
                                + e.getMessage()
                );

            } catch (InvalidStudentException
                     | DuplicateStudentIdException
                     | DuplicateEmailException
                     | StudentNotFoundException
                     | InvalidStatusTransitionException e) {
                console.showError(e.getMessage());

            } catch (Exception e) {
                console.showError(
                        "An unexpected error occurred: "
                                + e.getMessage()
                );
            }
            if (running) {
                console.pause();
            }
        }
console.close();
    }
    private static  Student readStudent(
            ConsoleUtils console){
        int id =
                console.readPositiveInt(
                        "Enter student ID: "
                );
        String name =
                console.readString(
                        "Enter student name: "
                );
        String email =
                console.readString(
                        "Enter student email: "
                );
        int age =
                console.readAge(
                        "Enter student age: "
                );
        double gpa =
                console.readGpa(
                        "Enter student GPA: "
                );
        String major =
                console.readString(
                        "Enter student major: "
                );
        LocalDate enrollmentDate =
                console.readDate(
                        "Enter enrollment date (YYYY-MM-DD): "
                );
        StudentStatus status =
                console.readStatus(
                        "Enter status " +
                                "(ACTIVE,GRADUATED,SUSPENDED,DROPPED): "
                );
        return new Student(
                id,
                name,
                email,
                age,
                gpa,
                major,
                enrollmentDate,
                status
        );
    }
   // --------------------------------------------------------------------------------------
   //                                       MAIN MENU
   // --------------------------------------------------------------------------------------
    private static void displayMainMenu() {
        System.out.println();
        System.out.println("--------------- MENU ----------------");
        System.out.println("1. Add a single student");
        System.out.println("2. Add multiple students in bulk");
        System.out.println("3. View all students");
        System.out.println("4. Find a student by ID");
        System.out.println("5. Search students by name");
        System.out.println("6. View students by major");
        System.out.println("7. Update a student's GPA");
        System.out.println("8. Change a student's status");
        System.out.println("9. Graduate a student");
        System.out.println("10. Show top students by GPA");
        System.out.println("11. Show summary statistics by status");
        System.out.println("12. Sort students");
        System.out.println("13. Save and exit");
        System.out.println("-------------------------------------");
    }

    // -----------------------------------------------------------------------------
    //                                 1. ADDING A SINGLE STUDENT
    // -----------------------------------------------------------------------------
    private static void addingSingleStudent(
            ConsoleUtils console,
            StudentService service){
        Student student = readStudent(console);
        service.registerStudent(student);
        console.showSuccess(
                "Student registered successfully."
        );

    }

    // ---------------------------------------------------------------------------
    //                           2. ADDING STUDENTS IN BULK
    // ---------------------------------------------------------------------------
    private static void addStudentsInBulk(
            ConsoleUtils console,
            StudentService service){
        int numberOfStudents =
                console.readPositiveInt(
                        "How many students to add? "
                );
        List<Student> students = new ArrayList<>();
        for(int i = 1; i<= numberOfStudents; i++) {
            console.showMessage("");
            console.showMessage(
                    "Entering student "
                            + i
                            + " of "
                            + numberOfStudents
            );

            Student student = readStudent(console);
            students.add(student);
        }
        List<Student>savedStudents =
                service.registerStudents(students);

                console.showSuccess(
                        savedStudents.size()
                        + " students registered successfully."
                );
        }
        // -------------------------------------------------------------------------
        //                            3. VIEWING ALL STUDENTS
        // -------------------------------------------------------------------------
        private static void viewStudents(
                ConsoleUtils console,
                StudentService service){
        int page =
                console.readNonNegativeInt(
                        "Enter page number (starting from 0): "
                );
        int pageSize =
                console.readPositiveInt(
                        "Enter page size: "
                );
        List<Student>students =
                service.findAll(page,pageSize);
        if(students.isEmpty()){
            console.showMessage(
                    "No students found on this page"
            );
            return;
        }
        console.displayStudents(students);
        }

        // ------------------------------------------------------------------
        //                      4. FINDING STUDENT BY ID
        // ------------------------------------------------------------------
        private static void findStudentByID(
                ConsoleUtils console,
                StudentService service){
        int id =
                console.readPositiveInt(
                        "Enter student ID: "
                );
        Student student =
                service.findById(id);
        console.displayStudent(student);
        }

        // -----------------------------------------------------------------------
        //                           5. SEARCHING STUDENTS BY NAME
        // -----------------------------------------------------------------------
        private static void searchStudentsByName(
                ConsoleUtils console,
                StudentService service){
        String query =
                console.readString(
                        "Enter full name or what name to search contains: "
                        );
        List<Student>students =
                service.searchByName(query);
        if(students.isEmpty()){
            console.showMessage(
                    "No students found matching the name or containing: " + query
            );
            return;
        }
            console.displayStudents(students);
        }

        // ------------------------------------------------------------------
        //                     6. VIEWING STUDENTS BY MAJOR
        // ------------------------------------------------------------------
        private static void viewStudentsByMajor(
                ConsoleUtils console,
                StudentService service){
        String major =
                console.readString(
                        "Enter major: "
                );
        List<Student>students =
                service.findByMajor(major);
        if(students.isEmpty()){
            console.showMessage(
                    "No students found in major: " + major
            );
            return;
        }
        console.displayStudents(students);
        }

        // ------------------------------------------------------
       //                 7. UPDATING STUDENT GPA
      //  -------------------------------------------------------
        private static  void updateStudentGPA(
                ConsoleUtils console,
                StudentService service){
        int id = console.readPositiveInt(
                "Enter student ID: "
        );
        double newGpa =
                console.readGpa(
                        "Enter new GPA: "
                );
        service.updateStudentGpa(id,newGpa);
        console.showSuccess(
                "Student GPA updated successfully."
        );
        }

        // ------------------------------------------------------
        //                8.  CHANGING STUDENT STATUS
        // ------------------------------------------------------

        private static void changeStudentStatus(
                ConsoleUtils console,
                StudentService service){
        int id =
                console.readPositiveInt(
                        "Enter student ID: "
                );
        StudentStatus status =
                console.readStatus(
                        "Enter new status "
                        + "(ACTIVE,GRADUATED,SUSPENDED,DROPPED): "
                );

        Student student = service.findById(id);
        if(student.getStatus() == status){
            console.showMessage(
                    "Student already in this  status: " + status
            );
            return;
        }
            service.changeStudentStatus(id,status);
        console.showSuccess(
                "Student status updated successfully."
        );
        }

        // ------------------------------------------------------
       //               9. GRADUATING STUDENT
       // -------------------------------------------------------
        private static void graduateStudent(
                ConsoleUtils console,
                StudentService service){
        int id =
                console.readPositiveInt(
                        "Enter student ID: "
                );
        Student student = service.findById(id);
        if(student.getStatus() == StudentStatus.GRADUATED){
            console.showMessage(
                    "Student is already graduated."
            );
            return;
        }
            service.graduateStudent(id);
            console.showSuccess(
                    "Student graduated successfully."
            );
        }

        // --------------------------------------------------------
        //                 10. SHOWING TOP 10 STUDENTS
        // --------------------------------------------------------
        private static void showTopStudents(
                ConsoleUtils console,
                StudentService service){
            List<Student>students =
                    service.getTopStudents(10);
            console.displayStudents(students);
        }

        // ---------------------------------------------------------
       //            11.   SUMMARY BY STATUS
       // ----------------------------------------------------------
      private static void showSummaryByStatus(
              ConsoleUtils console,
              StudentService service){
        Map<StudentStatus,Long> summary =
                service.countByStatus();
        console.showMessage("");
        console.showMessage("------STUDENT STATUS SUMMARY-----");
        for(StudentStatus status : StudentStatus.values()){
            long count =
                    summary.getOrDefault(status,0L);
            console.showMessage(
                    status + ": " + count
            );
        }
        console.showMessage("---------------------------------");
      }


      // -----------------------------------------------------------
      //                     12. SORTING VIA COMPARATOR
      // -----------------------------------------------------------
    private static void displaySortMenu() {
        System.out.println();
        System.out.println("--------- SORT STUDENTS ---------");
        System.out.println("1. Sort by name");
        System.out.println("2. Sort by enrollment date");
        System.out.println("3. Sort by major");
        System.out.println("4. Sort by status");
        System.out.println("5. Return to main menu");
        System.out.println("---------------------------------");
    }
private  static void sortStudents(
        ConsoleUtils console,
        StudentService service){
        boolean sorting = true;
        while (sorting){
            displaySortMenu();
            int choice =
                    console.readMenuChoice(1,5);
            switch (choice){
                case 1 -> {
                    List<Student>students =
                            service.sortStudents(1);
                    console.showMessage(
                            "Students sorted by name:"
                    );
                    console.displayStudents(students);
                }
                case 2 -> {
                    List<Student>students =
                            service.sortStudents(2);
                    console.showMessage(
                            "Students sorted by enrollment date:"
                    );
                    console.displayStudents(students);
                }
                case 3 -> {
                    List<Student>students =
                            service.sortStudents(3);
                    console.showMessage(
                            "Students sorted by major:"
                    );
                    console.displayStudents(students);
                }
                case 4 -> {
                    List<Student>students =
                            service.sortStudents(4);
                    console.showMessage(
                            "Students sorted by status:"
                    );
                    console.displayStudents(students);
                }
                case 5 -> sorting = false;
            }
            if(sorting){
                console.pause();
            }
        }
}
    }
