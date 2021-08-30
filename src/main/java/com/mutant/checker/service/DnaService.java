package com.mutant.checker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mutant.checker.service.dto.StatsResponseDTO;

public interface DnaService {
    boolean isMutant(String[] dna) throws JsonProcessingException;
    StatsResponseDTO stats();
}
