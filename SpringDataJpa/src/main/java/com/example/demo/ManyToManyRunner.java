package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class ManyToManyRunner {
    @Bean
    CommandLineRunner manyToMany(StudentRepository studentRepository) {
        return args -> {
            Faker faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu",
                    firstName.toLowerCase(Locale.ROOT),
                    lastName.toLowerCase());

            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 28));

            StudentIdCard studentIdCard = new StudentIdCard(
                    "123456789",
                    student);

            student.addBook(
                    new Book("Clean Code", LocalDateTime.now().minusDays(4)));
            student.addBook(
                    new Book("Think and Grow Reach", LocalDateTime.now()));
            student.addBook(
                    new Book("Spring Data JPA", LocalDateTime.now().minusYears(1)));

            student.setStudentIdCard(studentIdCard);

            student.addEnrollment(new Enrollment(
                    new EnrollmentId(1L, 1L),
                    student,
                    new Course("Computer Science", "IT"),
                    LocalDateTime.now())
            );
            student.addEnrollment(new Enrollment(
                    new EnrollmentId(1L, 2L),
                    student,
                    new Course("Amigoscode Spring Data JPA", "IT"),
                    LocalDateTime.now().minusMonths(2L))
            );

            studentRepository.save(student);

        };
    }
}
