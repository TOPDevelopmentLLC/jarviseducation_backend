package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    List<Code> findByIdIn(List<Long> ids);
}
