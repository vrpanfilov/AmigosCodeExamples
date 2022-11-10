package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByFirstNameAndAge(
            String firstName, Integer age);

    <S extends Student> List<S> findByAgeIsGreaterThan(Integer age);

    @Query("select s from Student s where s.age > ?1")
    List<Student> findByAgeGreaterThanEqual(Integer age);
}
