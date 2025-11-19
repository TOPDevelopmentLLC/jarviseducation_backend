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

    public Administrator createAdministrator(String name) {
        return administratorRepository.save(new Administrator(name));
    }

    public void deleteAdministrator(Long id) {
        if (!administratorRepository.existsById(id)) {
            throw new RuntimeException("Administrator not found with id: " + id);
        }
        administratorRepository.deleteById(id);
    }

}
