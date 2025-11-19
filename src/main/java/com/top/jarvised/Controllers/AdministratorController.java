package com.top.jarvised.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.Entities.Administrator;
import com.top.jarvised.Services.AdministratorService;

@RestController
public class AdministratorController {
    
    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping("/administrator")
    public Administrator createAdministrator(@RequestBody Map<String, String> postRequest) {
        if (!postRequest.containsKey("name")) {
            throw new RuntimeException("Request is missing the name property");
        }
        return administratorService.createAdministrator(postRequest.get("name"));
    }

    @GetMapping("/administrators")
    public List<Administrator> getAllAdministrators() {
        return administratorService.getAllAdministrators();
    }
}
