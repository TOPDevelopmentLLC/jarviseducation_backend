package com.top.jarvised.Repositories;

import com.top.jarvised.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReport_IdOrderByTimestampDesc(Long reportId);
}
