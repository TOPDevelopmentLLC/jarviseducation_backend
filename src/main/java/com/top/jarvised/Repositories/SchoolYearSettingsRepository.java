package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.SchoolYearSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolYearSettingsRepository extends JpaRepository<SchoolYearSettings, Long> {
    List<SchoolYearSettings> findBySchoolId(Long schoolId);
    Optional<SchoolYearSettings> findByIdAndSchoolId(Long id, Long schoolId);
    Optional<SchoolYearSettings> findBySchoolIdAndIsActiveTrue(Long schoolId);
}
