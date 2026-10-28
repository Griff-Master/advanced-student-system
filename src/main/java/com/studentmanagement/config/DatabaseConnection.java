package com.studentmanagement.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
private static  final String DEFAULT_URL =
        "jdbc:mysql://localHost:3306/student_management";
    private static final String DEFAULT_USERNAME = "root";
private DatabaseConnection(){
    // preventing creation of instances from this class
}

public static Connection getConnection() throws SQLException{
    String URL = System.getenv()
            .getOrDefault("DB_URL", DEFAULT_URL);
    String username = System.getenv()
            .getOrDefault("DB_USERNAME", DEFAULT_USERNAME);
    String password  =System.getenv("DB_PASSWORD");
    if(password == null ){
        password = "";
    }
    return DriverManager.getConnection(URL,username,password);
}
}
