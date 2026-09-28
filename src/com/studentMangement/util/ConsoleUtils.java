package com.studentMangement.util;
import com.studentMangement.entity.Student;
import com.studentMangement.entity.StudentStatus;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
public class ConsoleUtils {
    private final Scanner scanner;

    public ConsoleUtils() {
        scanner = new Scanner(System.in);

    }

    public String readString(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if(!input.isEmpty()){
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }
    public int readInt(String prompt){
        while(true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try{
                return Integer.parseInt(input);
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid integer.");
            }
        }
    }
    public int readPositiveInt(String prompt){
        while(true){
          int value = readInt(prompt);
          if(value > 0){
              return value;
          }
            System.out.println("Please enter positive integer.");
        }
    }
    public int readNonNegativeInt(String prompt){
        while(true){
            int value = readInt(prompt);
            if(value >= 0){
                return value;
            }
            System.out.println(
                    "Please enter 0 or positive integer."
            );
        }
    }
    public double readDouble(String prompt){
        while(true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }
    public double readGpa(String prompt){
        while(true){
            double gpa = readDouble(prompt);
            if(gpa >= 0.0 && gpa<=4.0){
                return gpa;
            }
            System.out.println("Gpa must be between 0.0 and 4.0.");
        }
    }
    public int readAge(String prompt){
        while(true){
       int age = readInt(prompt);
       if(age >= 16 && age <= 60 ){
           return age;
       }
            System.out.println("Age must be between 16 and 60.");
        }
    }
    public LocalDate readDate(String prompt){
        while(true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try{
                return LocalDate.parse(input);
            }catch (DateTimeParseException e){
                System.out.println(
                        "Please enter a valid date in the format YYYY-MM-DD."
                );
            }
        }
    }
    public StudentStatus readStatus(String prompt){
        while(true){
            System.out.println(prompt);
            String input =scanner.nextLine().trim();
            try{
                return StudentStatus.valueOf(input.toUpperCase());
            }catch (IllegalArgumentException e){
                System.out.println(
                        "Invalid status. Use ACTIVE, GRADUATED, SUSPENDED OR DROPPED."
                );
            }
        }
    }


    public int readMenuChoice(int min, int max){
        while(true){
            int choice = readInt("Enter your choice: ");
            if(choice >= min && choice <= max){
                return choice;
            }
            System.out.println(
                    "Please choose an option between " + min + " and " + max + "."
            );
        }
    }
    public void displayStudent(Student student){
        System.out.println("------------------------------------");
        System.out.println("ID             : " + student.getId());
        System.out.println("Name           : " + student.getName());
        System.out.println("Email          : " + student.getEmail());
        System.out.println("Age            : " + student.getAge());
        System.out.println("GPA            : " + student.getGpa());
        System.out.println("Major          : " + student.getMajor());
        System.out.println("Enrollment Date: " + student.getEnrollmentDate());
        System.out.println("Status         : " + student.getStatus());
        System.out.println("------------------------------------");
    }
    public void displayStudents(List<Student> students){
        if(students.isEmpty()){
            System.out.println("No students found.");
            return;
        }
        for(Student student : students){
            displayStudent(student);
        }
    }
    public void showSuccess(String message){
        System.out.println("SUCCESS: " + message);
    }
    public void showError(String message){
        System.out.println("ERROR: " + message);
    }
    public void showMessage(String message){
        System.out.println(message);
    }
    public void pause(){
        System.out.println();
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }
   public void close(){
        scanner.close();
   }
}
