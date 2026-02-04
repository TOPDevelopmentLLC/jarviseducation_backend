package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.student WHERE r.student.id = :studentId")
    List<Report> findByStudentId(Long studentId);

    @Query("SELECT r FROM Report r LEFT JOIN FETCH r.student")
    List<Report> findAllWithStudent();
}
