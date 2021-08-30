package com.mutant.checker.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DnaService {
    boolean isMutant(String[] dna) throws JsonProcessingException;
}
