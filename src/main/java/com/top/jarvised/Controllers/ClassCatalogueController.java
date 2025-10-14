package com.top.jarvised.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.Entities.ClassCatalogueItem;
import com.top.jarvised.Services.ClassCatalogueService;

@RestController
public class ClassCatalogueController {

    private ClassCatalogueService classCatalogueService;

    @Autowired
    public ClassCatalogueController(ClassCatalogueService classCatalogueService) {
        this.classCatalogueService = classCatalogueService;
    }

    @GetMapping("/classcatalogue")
    public List<ClassCatalogueItem> getAllClasses() {
        return classCatalogueService.getAllClasses();
    }

    @PostMapping("/classcatalogue")
    public ClassCatalogueItem createClass(@RequestBody ClassCatalogueItem classCatalogue) {
        return classCatalogueService.createClass(classCatalogue);
    }
    
}
