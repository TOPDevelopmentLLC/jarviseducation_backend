package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findBySchoolYearSettingsId(Long schoolYearSettingsId);
    Optional<Term> findByIdAndSchoolYearSettingsId(Long id, Long schoolYearSettingsId);
    Optional<Term> findBySchoolYearSettingsIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Long schoolYearSettingsId, LocalDate date1, LocalDate date2);
}
