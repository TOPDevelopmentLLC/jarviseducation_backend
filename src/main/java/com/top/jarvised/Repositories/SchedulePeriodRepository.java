package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.SchedulePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchedulePeriodRepository extends JpaRepository<SchedulePeriod, Long> {
    List<SchedulePeriod> findBySchoolYearSettingsIdOrderByPeriodNumber(Long schoolYearSettingsId);
    Optional<SchedulePeriod> findByIdAndSchoolYearSettingsId(Long id, Long schoolYearSettingsId);
    Optional<SchedulePeriod> findBySchoolYearSettingsIdAndStartTimeLessThanEqualAndEndTimeGreaterThan(
        Long schoolYearSettingsId, LocalTime time1, LocalTime time2);
}
