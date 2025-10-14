package com.top.jarvised.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class ClassCatalogueItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;
    private String courseDescription;
    // private Optional<Long> teacherId;

    public ClassCatalogueItem() {}

    public ClassCatalogueItem(String courseName, String courseDescription) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseDescription() {
        return this.courseDescription;
    }

}