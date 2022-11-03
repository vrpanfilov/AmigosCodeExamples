package com.example.demo;

import com.example.demo.student.Student;

import java.time.LocalDate;
import java.time.Month;

public class Tests {
    public static void main(String[] args) {
        Student alex = new Student(
                "Alex",
                "alex@gmail.com",
                LocalDate.of(2004, Month.JANUARY, 5)
        );
        alex = alex;
    }
}
