package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select s from Student s where s.email = ?1")
    Optional<Student> findByEmail(String email);

    @Query("select s from Student s where s.firstName = ?1 and s.age = ?2")
    Optional<Student> findByFirstNameAndAge(
            String firstName, Integer age);

    <S extends Student> List<S> findByAgeIsGreaterThan(Integer age);

    @Query("select s from Student s where s.age > ?1")
    List<Student> findByAgeGreaterThanEqual(Integer age);

    @Query(value = "select * from student where first_name = ?1 and age >= ?2",
            nativeQuery = true)
    List<Student> findByFirstNameAndAgeGreaterThanEqualNative(
            String firstName, Integer age);

    @Query("select s from Student s where s.firstName = :firstName and s.age >= :age")
    List<Student> findByFirstNameAndAgeNamed(
            @Param("firstName") String firstName,
            @Param("age") Integer age);

    @Transactional
    @Modifying
    @Query("delete from Student s where s.id = ?1")
    int deleteStudentById(Long id);
}
