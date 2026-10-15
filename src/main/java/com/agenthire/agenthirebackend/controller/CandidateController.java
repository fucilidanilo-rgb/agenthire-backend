package com.agenthire.agenthirebackend.controller;

import com.agenthire.agenthirebackend.dto.CandidateDto;
import com.agenthire.agenthirebackend.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:4200")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public CandidateDto createCandidate(
            @Valid @RequestBody CandidateDto candidateDto
    ) {
        return candidateService.saveCandidate(candidateDto);
    }

    @GetMapping
    public List<CandidateDto> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public CandidateDto getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidateById(id);
    }

    @PutMapping("/{id}")
    public CandidateDto updateCandidate(
            @PathVariable Long id,
            @Valid @RequestBody CandidateDto candidateDto
    ) {
        return candidateService.updateCandidate(id, candidateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }
}