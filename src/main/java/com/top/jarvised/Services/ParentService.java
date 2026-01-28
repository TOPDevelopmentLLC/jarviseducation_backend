package com.top.jarvised.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.top.jarvised.Entities.Parent;
import com.top.jarvised.Repositories.ParentRepository;

@Service
public class ParentService {
    
    private ParentRepository parentRepository;

    @Autowired
    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Parent> getAllStudents() {
        return parentRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Parent createParent(Parent parent) {
        return parentRepository.save(parent);
    }

    public void deleteParent(Long id) {
        if (!parentRepository.existsById(id)) {
            throw new RuntimeException("Parent not found with id: " + id);
        }
        parentRepository.deleteById(id);
    }

}
