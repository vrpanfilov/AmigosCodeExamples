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
            studentRepository.saveAll(List.of(maria, ahmed, jamal));

//            repositoryMethods(studentRepository);
            queryMethods(studentRepository);
        };
    }

    private void queryMethods(StudentRepository studentRepository) {
        String email = "ahmed.ali@amigoscode.edu";
        System.out.println();
        studentRepository.findByEmail(email)
                .ifPresentOrElse(
                        student -> {
                            System.out.println("Student with email '" + email + "':");
                            System.out.println(student);
                        },
                        () -> System.out.println(
                                "Student with email " + email + " not found"));

        System.out.println();
        String wrongEmail = "email@gmail.com";
        studentRepository.findByEmail(wrongEmail)
                .ifPresentOrElse(
                        student -> {
                            System.out.println("Student with email '" + wrongEmail + "':");
                            System.out.println(student);
                        },
                        () -> System.out.println(
                                "Student with email '" + wrongEmail + "' not found"));

        System.out.println();
        String firstName = "Ahmed";
        Integer age = 18;
        studentRepository.findByFirstNameAndAge(firstName, age)
                .ifPresentOrElse(
                        student -> {
                            System.out.println("Student " + firstName + " of age " + age + " found:");
                            System.out.println(student);
                        },
                        () -> System.out.println(
                                "Student with firstName " + firstName +
                                        " and of age " + age + " not found"));
        System.out.println();
        Integer wrongAge = 28;
        studentRepository.findByFirstNameAndAge(firstName, wrongAge)
                .ifPresentOrElse(
                        student -> {
                            System.out.println("Student found:");
                            System.out.println(student);
                        },
                        () -> System.out.println(
                                "Student with firstName " + firstName +
                                        " and of age " + wrongAge + " not found"));

        System.out.println();
        Integer thresholdAge = 20;
        System.out.println("Students older than " + thresholdAge + ":");
        studentRepository.findByAgeIsGreaterThan(thresholdAge).forEach(System.out::println);

        // Это работает неверно, потому что в репозитории @Query
        // перекрывает именной запрос
        System.out.println();
        Integer theAge = 21;
        System.out.println("Students older than " + theAge + ":");
        studentRepository.findByAgeGreaterThanEqual(theAge).forEach(System.out::println);
    }

    private void repositoryMethods(StudentRepository studentRepository) {
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
    }
}
