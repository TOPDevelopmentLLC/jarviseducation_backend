package com.top.jarvised.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.top.jarvised.SchoolContext;
import com.top.jarvised.DTOs.PointsSystemResponse;
import com.top.jarvised.DTOs.UpdatePointsSystemRequest;
import com.top.jarvised.Entities.PointsSystem;
import com.top.jarvised.Repositories.PointsSystemRepository;

@Service
public class PointsSystemService {

    private final PointsSystemRepository pointsSystemRepository;

    @Autowired
    public PointsSystemService(PointsSystemRepository pointsSystemRepository) {
        this.pointsSystemRepository = pointsSystemRepository;
    }

    /**
     * Gets the points system for a user account.
     * If no points system exists, creates a default one.
     * @param userAccountId The user account ID
     * @return PointsSystemResponse with the points system data
     */
    public PointsSystemResponse getPointsSystem(Long userAccountId) {
        // Clear context to ensure we're working with master DB
        SchoolContext.clear();

        PointsSystem pointsSystem = pointsSystemRepository.findByUserAccountId(userAccountId)
            .orElseGet(() -> createDefaultPointsSystem(userAccountId));

        return new PointsSystemResponse(pointsSystem);
    }

    /**
     * Updates the points system for a user account.
     * Only updates fields that are provided (non-null).
     * @param userAccountId The user account ID
     * @param request The update request with new values
     * @return PointsSystemResponse with the updated points system data
     */
    @Transactional
    public PointsSystemResponse updatePointsSystem(Long userAccountId, UpdatePointsSystemRequest request) {
        // Clear context to ensure we're working with master DB
        SchoolContext.clear();

        PointsSystem pointsSystem = pointsSystemRepository.findByUserAccountId(userAccountId)
            .orElseGet(() -> createDefaultPointsSystem(userAccountId));

        if (request.getAttendanceDeduction() != null) {
            pointsSystem.setAttendanceDeduction(request.getAttendanceDeduction());
        }
        if (request.getBehaviorDeduction() != null) {
            pointsSystem.setBehaviorDeduction(request.getBehaviorDeduction());
        }
        if (request.getConflictDeduction() != null) {
            pointsSystem.setConflictDeduction(request.getConflictDeduction());
        }
        if (request.getExpelledDeduction() != null) {
            pointsSystem.setExpelledDeduction(request.getExpelledDeduction());
        }
        if (request.getMoodDeduction() != null) {
            pointsSystem.setMoodDeduction(request.getMoodDeduction());
        }
        if (request.getSecludedDeduction() != null) {
            pointsSystem.setSecludedDeduction(request.getSecludedDeduction());
        }
        if (request.getSipDeduction() != null) {
            pointsSystem.setSipDeduction(request.getSipDeduction());
        }
        if (request.getDailyIncrease() != null) {
            pointsSystem.setDailyIncrease(request.getDailyIncrease());
        }

        pointsSystem = pointsSystemRepository.save(pointsSystem);
        return new PointsSystemResponse(pointsSystem);
    }

    /**
     * Creates a default points system for a user account.
     * All deductions default to 0.
     * @param userAccountId The user account ID
     * @return The created PointsSystem entity
     */
    private PointsSystem createDefaultPointsSystem(Long userAccountId) {
        PointsSystem pointsSystem = new PointsSystem(userAccountId);
        return pointsSystemRepository.save(pointsSystem);
    }
}
