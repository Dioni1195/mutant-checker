package com.mutant.checker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mutant.checker.service.DnaService;
import com.mutant.checker.service.dto.CheckDnaRequestDTO;
import com.mutant.checker.service.dto.StatsResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class DnaController
 * @date 26/08/2021
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/mutant")
public class DnaController {

    private final DnaService dnaService;

    DnaController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @PostMapping("/")
    public ResponseEntity<Boolean> checkDNA(@Valid @RequestBody CheckDnaRequestDTO dto) throws JsonProcessingException {
        return new ResponseEntity<>(dnaService.isMutant(dto.getDna()), HttpStatus.OK);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDTO> stats() {
        return new ResponseEntity<>(dnaService.stats(), HttpStatus.OK);
    }
 }
