
package com.agenthire.agenthirebackend.repository;

import com.agenthire.agenthirebackend.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // Recupera soltanto i candidati di un utente
    List<Candidate> findByOwnerId(Long ownerId);

    // Cerca un candidato verificando anche il proprietario
    Optional<Candidate> findByIdAndOwnerId(Long id, Long ownerId);
}
