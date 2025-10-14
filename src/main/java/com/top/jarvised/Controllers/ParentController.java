package com.top.jarvised.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.Entities.Parent;
import com.top.jarvised.Services.ParentService;

@RestController
public class ParentController {

    private ParentService parentService;

    @Autowired
    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/parents")
    public List<Parent> getAllStudents() {
        return parentService.getAllStudents();
    }

    @PostMapping("/parents")
    public Parent createParent(@RequestBody Parent parent) {
        return parentService.createParent(parent);
    }
    
}
