package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student maria = new Student(null,
                    "Maria",
                    "Jones",
                    "maria.jones@amigoscode.edu",
                    21
            );
            Student ahmed = new Student(null,
                    "Ahmed",
                    "Ali",
                    "ahmed.ali@amigoscode.edu",
                    18
            );
            Student jamal = new Student(null,
                    "Jamal",
                    "Bond",
                    "jamal.bond@gmail.com",
                    32);

            System.out.println("Adding maria, ahmed and jamal");
            studentRepository.saveAll(List.of(maria, ahmed,
                    jamal));

            System.out.println();
            System.out.println("Number of students: " + studentRepository.count());


            System.out.println();
            studentRepository.findById(2L).ifPresentOrElse(System.out::println,
                    () -> System.out.println("Student with id 2 not found"));

            System.out.println();
            studentRepository.findById(4L).ifPresentOrElse(System.out::println,
                    () -> System.out.println("Student with id 4 not found"));

            System.out.println();
            System.out.println("Select all students");
            studentRepository.findAll().forEach(System.out::println);

            System.out.println();
            System.out.println("Delete maria");
            studentRepository.deleteById(1L);

            System.out.println();
            System.out.println("Number of students: " + studentRepository.count());
        };
    }
}
