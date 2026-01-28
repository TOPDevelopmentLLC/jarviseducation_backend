package com.top.jarvised.Services;

import com.top.jarvised.DTOs.CreateCodeRequest;
import com.top.jarvised.DTOs.UpdateCodeRequest;
import com.top.jarvised.Entities.Code;
import com.top.jarvised.Repositories.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeService {

    private CodeRepository codeRepository;

    @Autowired
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    /**
     * Get all codes
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Code> getAllCodes() {
        return codeRepository.findAll();
    }

    /**
     * Get a specific code by ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Code getCodeById(Long codeId) {
        return codeRepository.findById(codeId)
            .orElseThrow(() -> new RuntimeException("Code not found"));
    }

    /**
     * Create a new code
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Code createCode(CreateCodeRequest request) {
        if (request.getCode() == null || request.getCode().trim().isEmpty()) {
            throw new RuntimeException("Code value is required");
        }

        Code code = new Code(request.getCode(), request.getDescription());
        return codeRepository.save(code);
    }

    /**
     * Update an existing code
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Code updateCode(Long codeId, UpdateCodeRequest request) {
        Code code = codeRepository.findById(codeId)
            .orElseThrow(() -> new RuntimeException("Code not found"));

        if (request.getCode() != null && !request.getCode().trim().isEmpty()) {
            code.setCode(request.getCode());
        }

        if (request.getDescription() != null) {
            code.setDescription(request.getDescription());
        }

        return codeRepository.save(code);
    }

    /**
     * Delete a code
     * Automatically removes the code from all teams it's assigned to
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteCode(Long codeId) {
        Code code = codeRepository.findById(codeId)
            .orElseThrow(() -> new RuntimeException("Code not found"));

        // Remove this code from all teams it's assigned to
        code.getTeams().forEach(team -> team.removeCode(code));

        // Now delete the code
        codeRepository.delete(code);
    }
}
