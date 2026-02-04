package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findAllByOrderByIdDesc();

    public List<Report> findByCreatedDateBetweenOrderByThreadUpdatedDateDesc(LocalDateTime since, LocalDateTime until);

    @Modifying
    @Transactional
    @Query("UPDATE Report r SET r.threadUpdatedDate = :threadUpdatedDate WHERE r.id = :id")
    void updateThreadUpdatedDateById(@Param("id") Integer id, @Param("threadUpdatedDate") LocalDateTime threadUpdatedDate);
}

