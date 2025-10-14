package com.top.jarvised.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.top.jarvised.Entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // @Query("SELECT * FROM Student s WHERE s.classIDList != null && s.classIDList.contains(?1)")
    // Optional<List<Student>> findAllStudentsThatContainClassId(Integer classId);

}