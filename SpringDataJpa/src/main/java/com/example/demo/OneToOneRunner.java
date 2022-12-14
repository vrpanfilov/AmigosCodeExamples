package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class OneToOneRunner {
//    @Bean
    CommandLineRunner oneToOne(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository
    ) {
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
            studentIdCardRepository.save(studentIdCard);

            studentRepository.findById(1L).ifPresent(System.out::println);

            studentIdCardRepository.findById(1L)
                    .ifPresent(System.out::println);

            studentIdCardRepository.deleteById(1L);
        };
    }
}
