package com.top.jarvised.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.top.jarvised.Entities.Parent;
import com.top.jarvised.Repositories.ParentRepository;

@Service
public class ParentService {
    
    private ParentRepository parentRepository;

    @Autowired
    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    public List<Parent> getAllStudents() {
        return parentRepository.findAll();
    }

    public Parent createParent(Parent parent) {
        return parentRepository.save(parent);
    }

}
