package com.top.jarvised.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.top.jarvised.Entities.PointsSystem;

public interface PointsSystemRepository extends JpaRepository<PointsSystem, Long> {
    Optional<PointsSystem> findByUserAccountId(Long userAccountId);
}
