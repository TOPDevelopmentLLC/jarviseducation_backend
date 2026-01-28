package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findBySchoolYearSettingsId(Long schoolYearSettingsId);
    Optional<Holiday> findByIdAndSchoolYearSettingsId(Long id, Long schoolYearSettingsId);
    Optional<Holiday> findBySchoolYearSettingsIdAndDate(Long schoolYearSettingsId, LocalDate date);
    List<Holiday> findBySchoolYearSettingsIdAndDateBetween(Long schoolYearSettingsId,
                                                           LocalDate start, LocalDate end);
}
