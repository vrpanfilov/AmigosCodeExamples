package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Component
public class OneToManyRunner {
    @Bean
    CommandLineRunner oneToMany(StudentRepository studentRepository) {
        return args -> {
            Faker faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu",
                    firstName.toLowerCase(Locale.ROOT),
                    lastName.toLowerCase());

            Student student = new Student(null,
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 28));

            StudentIdCard studentIdCard = new StudentIdCard(
                    null,
                    "123456789",
                    student);

            student.addBook(
                    new Book("Clean Code", LocalDateTime.now().minusDays(4)));
            student.addBook(
                    new Book("Think and Grow Reach", LocalDateTime.now()));
            student.addBook(
                    new Book("Spring Data JPA", LocalDateTime.now().minusYears(1)));

            student.setStudentIdCard(studentIdCard);

            studentRepository.save(student);

            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        System.out.println("Fetch book lazy");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(s.getFirstName() +
                                    " borrowed " + book.getBookName());
                        });
                    });
        };
    }
}
