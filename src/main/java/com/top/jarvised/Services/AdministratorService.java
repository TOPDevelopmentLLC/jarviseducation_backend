package com.top.jarvised.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.top.jarvised.Entities.Administrator;
import com.top.jarvised.Repositories.AdministratorRepository;

@Service
public class AdministratorService {

    private AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    public Administrator createAdministrator(String id, String name) {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Request is missing a valid id property");
        }
        return administratorRepository.save(new Administrator(Long.valueOf(id), name));
    }
    
}
