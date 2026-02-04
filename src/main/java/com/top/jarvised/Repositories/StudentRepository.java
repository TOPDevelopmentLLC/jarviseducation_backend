package com.top.jarvised.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.top.jarvised.Entities.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByIsActiveTrue();

    // @Query("SELECT * FROM Student s WHERE s.classIDList != null && s.classIDList.contains(?1)")
    // Optional<List<Student>> findAllStudentsThatContainClassId(Integer classId);

}