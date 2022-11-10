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
            Student maria2 = new Student(null,
                    "Maria",
                    "Jones",
                    "maria2.jones@amigoscode.edu",
                    25
            );

            System.out.println("Adding maria, ahmed, jamal and maria2");
            studentRepository.saveAll(List.of(maria, ahmed, jamal, maria2));

            System.out.println();
//            repositoryMethods(studentRepository);
//            jpaQueryMethods(studentRepository);
//            nativeQueryMethods(studentRepository);
//            namedParameters(studentRepository);
            modifyingMethods(studentRepository);
        };
    }

    private void repositoryMethods(StudentRepository studentRepository) {
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
        studentRepository.deleteStudentById(1L);

        System.out.println();
        System.out.println("Number of students: " + studentRepository.count());
    }

    private void jpaQueryMethods(StudentRepository studentRepository) {
        String email = "ahmed.ali@amigoscode.edu";
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
        System.out.println("Next works wrong!");
        Integer theAge = 21;
        System.out.println("Students older than or equal to" + theAge + ":");
        studentRepository.findByAgeGreaterThanEqual(theAge).forEach(System.out::println);
    }

    private void nativeQueryMethods(StudentRepository studentRepository) {
        String firstName = "Maria";
        Integer age = 22;
        System.out.println("Marias of age >= " + age);
        studentRepository.findByFirstNameAndAgeGreaterThanEqualNative(firstName, age)
                .forEach(System.out::println);

        System.out.println();
        Integer newAge = 20;
        System.out.println("Marias of age >= " + newAge);
        studentRepository.findByFirstNameAndAgeGreaterThanEqualNative(firstName, newAge)
                .forEach(System.out::println);
    }

    private void namedParameters(StudentRepository studentRepository) {
        String firstName = "Maria";
        Integer age = 21;
        System.out.println("Marias of age >= " + age);
        studentRepository.findByFirstNameAndAgeNamed(firstName, age)
                .forEach(System.out::println);
    }

    private void modifyingMethods(StudentRepository studentRepository) {
        Long id = 4L;
        System.out.println("Delete the student with id " + id);
        int count = studentRepository.deleteStudentById(id);
        System.out.println("Deleted " + count + " rows");
    }
}
