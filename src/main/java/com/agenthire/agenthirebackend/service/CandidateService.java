
package com.agenthire.agenthirebackend.service;

import com.agenthire.agenthirebackend.dto.CandidateDto;
import com.agenthire.agenthirebackend.entity.AppUser;
import com.agenthire.agenthirebackend.entity.Candidate;
import com.agenthire.agenthirebackend.exception.CandidateNotFoundException;
import com.agenthire.agenthirebackend.repository.AppUserRepository;
import com.agenthire.agenthirebackend.repository.CandidateRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final AppUserRepository appUserRepository;

    public CandidateService(
            CandidateRepository candidateRepository,
            AppUserRepository appUserRepository
    ) {
        this.candidateRepository = candidateRepository;
        this.appUserRepository = appUserRepository;
    }

    // Recupera l'utente autenticato
    private AppUser getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Accesso non autorizzato");
        }

        String email = authentication.getName();

        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AccessDeniedException("Utente non trovato")
                );
    }

    // Impedisce modifiche agli account DEMO
    private AppUser getWritableUser() {

        AppUser user = getCurrentUser();

        if (!"USER".equals(user.getRole())) {
            throw new AccessDeniedException(
                    "L'account demo non puo modificare i dati"
            );
        }

        return user;
    }

    // CREATE
    public CandidateDto saveCandidate(CandidateDto candidateDto) {

        AppUser user = getWritableUser();

        Candidate candidate = convertToEntity(candidateDto);
        candidate.setOwner(user);

        Candidate savedCandidate = candidateRepository.save(candidate);

        return convertToDto(savedCandidate);
    }

    // READ ALL - soltanto i propri candidati
    public List<CandidateDto> getAllCandidates() {

        AppUser user = getCurrentUser();

        return candidateRepository.findByOwnerId(user.getId())
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // READ ONE - controlla il proprietario
    public CandidateDto getCandidateById(Long id) {

        AppUser user = getCurrentUser();

        Candidate candidate = candidateRepository
                .findByIdAndOwnerId(id, user.getId())
                .orElseThrow(() ->
                        new CandidateNotFoundException(
                                "Candidato non trovato"
                        )
                );

        return convertToDto(candidate);
    }

    // UPDATE
    public CandidateDto updateCandidate(
            Long id,
            CandidateDto updatedCandidateDto
    ) {

        AppUser user = getWritableUser();

        Candidate candidate = candidateRepository
                .findByIdAndOwnerId(id, user.getId())
                .orElseThrow(() ->
                        new CandidateNotFoundException(
                                "Candidato non trovato"
                        )
                );

        candidate.setName(updatedCandidateDto.getName());
        candidate.setEmail(updatedCandidateDto.getEmail());
        candidate.setTargetRole(updatedCandidateDto.getTargetRole());

        Candidate updatedCandidate = candidateRepository.save(candidate);

        return convertToDto(updatedCandidate);
    }

    // DELETE
    public void deleteCandidate(Long id) {

        AppUser user = getWritableUser();

        Candidate candidate = candidateRepository
                .findByIdAndOwnerId(id, user.getId())
                .orElseThrow(() ->
                        new CandidateNotFoundException(
                                "Candidato non trovato"
                        )
                );

        candidateRepository.delete(candidate);
    }

    // ENTITY -> DTO
    private CandidateDto convertToDto(Candidate candidate) {

        return new CandidateDto(
                candidate.getId(),
                candidate.getName(),
                candidate.getEmail(),
                candidate.getTargetRole()
        );
    }

    // DTO -> ENTITY
    private Candidate convertToEntity(CandidateDto dto) {

        Candidate candidate = new Candidate();

        candidate.setName(dto.getName());
        candidate.setEmail(dto.getEmail());
        candidate.setTargetRole(dto.getTargetRole());

        return candidate;
    }
}
