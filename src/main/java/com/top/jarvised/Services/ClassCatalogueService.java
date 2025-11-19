package com.top.jarvised.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.top.jarvised.Entities.ClassCatalogueItem;
import com.top.jarvised.Repositories.ClassCatalogueRepository;

@Service
public class ClassCatalogueService {
    
    private ClassCatalogueRepository classCatalogueRepository;

    @Autowired
    public ClassCatalogueService(ClassCatalogueRepository classCatalogueRepository) {
        this.classCatalogueRepository = classCatalogueRepository;
    }

    public List<ClassCatalogueItem> getAllClasses() {
        return classCatalogueRepository.findAll();
    }

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
