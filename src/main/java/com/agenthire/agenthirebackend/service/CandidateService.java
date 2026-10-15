package com.agenthire.agenthirebackend.service;

import com.agenthire.agenthirebackend.dto.CandidateDto;
import com.agenthire.agenthirebackend.entity.Candidate;
import com.agenthire.agenthirebackend.exception.CandidateNotFoundException;
import com.agenthire.agenthirebackend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public CandidateDto saveCandidate(CandidateDto candidateDto) {

        Candidate candidate = convertToEntity(candidateDto);

        Candidate savedCandidate = candidateRepository.save(candidate);

        return convertToDto(savedCandidate);
    }

    public List<CandidateDto> getAllCandidates() {

        return candidateRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public CandidateDto getCandidateById(Long id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new CandidateNotFoundException("Candidate non trovato con id: " + id)
                );

        return convertToDto(candidate);
    }

    public CandidateDto updateCandidate(
            Long id,
            CandidateDto updatedCandidateDto
    ) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new CandidateNotFoundException("Candidate non trovato con id: " + id)
                );

        candidate.setName(updatedCandidateDto.getName());
        candidate.setEmail(updatedCandidateDto.getEmail());
        candidate.setTargetRole(updatedCandidateDto.getTargetRole());

        Candidate updatedCandidate = candidateRepository.save(candidate);

        return convertToDto(updatedCandidate);
    }

    public void deleteCandidate(Long id) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new CandidateNotFoundException("Candidate non trovato con id: " + id)
                );

        candidateRepository.delete(candidate);
    }

    private CandidateDto convertToDto(Candidate candidate) {

        return new CandidateDto(
                candidate.getId(),
                candidate.getName(),
                candidate.getEmail(),
                candidate.getTargetRole()
        );
    }

    private Candidate convertToEntity(CandidateDto dto) {

        Candidate candidate = new Candidate();

        candidate.setName(dto.getName());
        candidate.setEmail(dto.getEmail());
        candidate.setTargetRole(dto.getTargetRole());

        return candidate;
    }
}