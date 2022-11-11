package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class FakerStudentsRunner {
//    @Bean
    CommandLineRunner fakerStudents(StudentRepository studentRepository) {
        return args -> {
            generateRamdomStudents(studentRepository);
            sorting(studentRepository);
            paging(studentRepository);
        };
    }

    private void generateRamdomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu",
                    firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 28));
            studentRepository.save(student);
        }
    }

    private void sorting(StudentRepository studentRepository) {
//            Sort sort = Sort.by(Sort.Direction.DESC, "firstName");
        Sort sort = Sort.by("firstName").ascending()
                .and(Sort.by("age").descending());
        sort = Sort.by("age").descending()
                .and(Sort.by("firstName").ascending());
        studentRepository.findAll(sort)
                .forEach(student -> System.out.println(
                        student.getFirstName() + " " + student.getAge()));
    }

    private void paging(StudentRepository studentRepository) {
        int pageNumber = 0;
        int pageSize = 5;
        boolean hasNextPage = true;
        while (hasNextPage) {
            PageRequest pageRequest = PageRequest.of(
                    pageNumber++,
                    pageSize,
                    Sort.by("firstName").descending());
            Page<Student> page = studentRepository.findAll(pageRequest);
            System.out.println(page);
            page.getContent().forEach(System.out::println);
            hasNextPage = page.hasNext();
        }
    }
}
