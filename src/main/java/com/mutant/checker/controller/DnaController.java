package com.mutant.checker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mutant.checker.service.DnaService;
import com.mutant.checker.service.dto.CheckDnaRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class DnaController
 * @date 26/08/2021
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class DnaController {

    private final DnaService dnaService;

    DnaController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @PostMapping("/mutant")
    public ResponseEntity<Boolean> checkDNA(@RequestBody CheckDnaRequestDTO dto) throws JsonProcessingException {
        return new ResponseEntity<>(dnaService.isMutant(dto.getDna()), HttpStatus.OK);
    }
 }
