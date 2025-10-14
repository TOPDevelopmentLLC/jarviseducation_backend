package com.top.jarvised.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.top.jarvised.Entities.Teacher;
import com.top.jarvised.Repositories.TeacherRepository;

@Service
public class TeacherService {

    private TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
}
