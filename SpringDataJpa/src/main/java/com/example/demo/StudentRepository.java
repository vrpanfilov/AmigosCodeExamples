package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByFirstNameAndAge(
            String firstName, Integer age);

    <S extends Student> List<S> findByAgeIsGreaterThan(Integer age);
}
