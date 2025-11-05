package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findBySchoolId(Long schoolId);
    Optional<Team> findByIdAndSchoolId(Long id, Long schoolId);
}
