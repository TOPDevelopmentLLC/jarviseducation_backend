package com.top.jarvised.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.top.jarvised.Entities.ClassCatalogueItem;
import com.top.jarvised.Repositories.ClassCatalogueRepository;

@Service
public class ClassCatalogueService {
    
    private ClassCatalogueRepository classCatalogueRepository;

    @Autowired
    public ClassCatalogueService(ClassCatalogueRepository classCatalogueRepository) {
        this.classCatalogueRepository = classCatalogueRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ClassCatalogueItem> getAllClasses() {
        return classCatalogueRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClassCatalogueItem createClass(ClassCatalogueItem classCatalogue) {
        return classCatalogueRepository.save(classCatalogue);
    }

    public void deleteClass(Long id) {
        if (!classCatalogueRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        classCatalogueRepository.deleteById(id);
    }

}
