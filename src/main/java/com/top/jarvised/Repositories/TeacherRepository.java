package com.top.jarvised.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.top.jarvised.Entities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}