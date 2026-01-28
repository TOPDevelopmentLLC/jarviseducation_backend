package com.top.jarvised.DTOs;

import com.top.jarvised.Entities.Student;

public class StudentResponse {
    private Long id;
    private String name;
    private Integer studentPoints;

    public StudentResponse() {}

    public StudentResponse(Student student, Integer studentPoints) {
        this.id = student.getId();
        this.name = student.getName();
        this.studentPoints = studentPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStudentPoints() {
        return studentPoints;
    }

    public void setStudentPoints(Integer studentPoints) {
        this.studentPoints = studentPoints;
    }
}
