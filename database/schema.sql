CREATE DATABASE IF NOT EXISTS student_management;

USE student_management;

CREATE TABLE IF NOT EXISTS students (
                                        id INT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    age INT NOT NULL,
    gpa DOUBLE NOT NULL,
    major VARCHAR(100) NOT NULL,
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,


    CONSTRAINT chk_student_id
        CHECK (id > 0),

    CONSTRAINT chk_student_age
        CHECK (age BETWEEN 16 AND 60),

    CONSTRAINT chk_student_gpa
        CHECK (gpa BETWEEN 0.0 AND 4.0),

    CONSTRAINT chk_student_email_format
        CHECK (
            email REGEXP '^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'
        ),

    CONSTRAINT chk_student_status
        CHECK (
            status IN ('ACTIVE', 'GRADUATED', 'SUSPENDED', 'DROPPED')
        )


    );
