package com.agenthire.agenthirebackend.repository;

import com.agenthire.agenthirebackend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}