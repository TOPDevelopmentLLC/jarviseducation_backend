package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.BreakPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BreakPeriodRepository extends JpaRepository<BreakPeriod, Long> {
    List<BreakPeriod> findBySchoolYearSettingsId(Long schoolYearSettingsId);
    Optional<BreakPeriod> findByIdAndSchoolYearSettingsId(Long id, Long schoolYearSettingsId);
    Optional<BreakPeriod> findBySchoolYearSettingsIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Long schoolYearSettingsId, LocalDate date1, LocalDate date2);
}
