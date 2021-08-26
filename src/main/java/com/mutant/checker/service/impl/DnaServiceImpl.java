package com.mutant.checker.service.impl;

import com.mutant.checker.service.DnaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class DnaServiceImpl
 * @date 26/08/2021
 */
@Service
@Slf4j
public class DnaServiceImpl implements DnaService {

    @Override
    public boolean isMutant(List<String> dna) {
        return false;
    }
}
