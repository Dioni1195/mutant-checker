package com.mutant.checker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutant.checker.AdnRepository;
import com.mutant.checker.config.exception.MCError;
import com.mutant.checker.config.exception.MCRuntimeException;
import com.mutant.checker.config.exception.NoMutantException;
import com.mutant.checker.service.DnaService;
import com.mutant.checker.service.dto.AdnRecord;
import com.mutant.checker.service.dto.ErrorDTO;
import com.mutant.checker.service.dto.StatsResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mutant.checker.config.exception.errorcodes.ServiceErrorCodes.*;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class DnaServiceImpl
 * @date 26/08/2021
 */
@Service
@Slf4j
public class DnaServiceImpl implements DnaService {
    private final AdnRepository adnRepository;
    private static final ObjectMapper mapper = new ObjectMapper();
    
    DnaServiceImpl(AdnRepository adnRepository){
        this.adnRepository = adnRepository;
    }

    @Override
    public boolean isMutant(String[] dna) throws JsonProcessingException {
        List<Boolean> resultList = new ArrayList<>();
        
        if(dna.length < 4) {
            throw new MCRuntimeException(new MCError(
                    HttpStatus.BAD_REQUEST, new ErrorDTO(
                    ERROR_TAMANO_MINIMO_CODE,
                    ERROR_TAMANO_MINIMO,
                    TYPE_E
            )));
        }
        
        for (int i = 0; i < dna.length; i++) {
            if(dna[i].length() != dna.length) {
                throw new MCRuntimeException(new MCError(
                        HttpStatus.BAD_REQUEST, new ErrorDTO(
                        ERROR_MATRIZ_NO_CUADRADA_CODE,
                        String.format(ERROR_MATRIZ_NO_CUADRADA, i, dna[i].length()),
                        TYPE_E
                )));
            }

            int length = dna.length;
            for (int j = 0; j < length; j++) {
                if (resultList.size() == 2) {
                    saveRecord(buildAdnRecord(dna, true));
                    return true;
                }
                
                if (j != length - 1) {
                    
                    if (dna[i].charAt(j) == dna[i].charAt(j + 1)) {
                        if(j + 2 < length - 1) {
                            if ((dna[i].charAt(j) == dna[i].charAt(j + 2)) && (dna[i].charAt(j) == dna[i].charAt(j + 3))){
                                resultList.add(true);
                                log.info(String.format("HORIZONTAL: %s == %s", dna[i].charAt(j), dna[i].charAt(j + 1)));
                            }
                        }
                    }
                    
                }
                
                if (i != length - 1) {
                    
                    if (dna[i].charAt(j) == dna[i + 1].charAt(j)) {
                        if (i + 2 < length - 1) {
                            if ((dna[i].charAt(j) == dna[i + 2].charAt(j)) && (dna[i].charAt(j) == dna[i + 3].charAt(j))) {
                                resultList.add(true);
                                log.info(String.format("VERTICAL: %s == %s", dna[i].charAt(j), dna[i + 1].charAt(j)));
                            }
                        }
                    }
                    
                }
                
                
                if ((j != length - 1) && (i != length - 1)) {
    
                    if (dna[i].charAt(j) == dna[i + 1].charAt(j + 1)) {
                        if ((i + 2 < length - 1) && (j + 2 < length - 1)) {
                            if ((dna[i].charAt(j) == dna[i + 2].charAt(j + 2)) && (dna[i].charAt(j) == dna[i + 3].charAt(j + 3))) {
                                resultList.add(true);
                                log.info(String.format("OBLICUO POSITIVO: %s == %s", dna[i].charAt(j), dna[i + 1].charAt(j + 1)));
                            }
                        }
                    }
    
                    if (j != 0 && dna[i].charAt(j) == dna[i + 1].charAt(j - 1)) {
                        if ((i + 2 < length - 1) && (j - 2 >= 1)) {
                            if ((dna[i].charAt(j) == dna[i + 2].charAt(j - 2)) && (dna[i].charAt(j) == dna[i + 3].charAt(j - 3))) {
                                resultList.add(true);
                                log.info(String.format("OBLICUO NEGATIVO: %s == %s", dna[i].charAt(j), dna[i + 1].charAt(j - 1)));
                            }
                        }
                    }
                }
                
            }
        }
    
        saveRecord(buildAdnRecord(dna, false));
        throw new NoMutantException(ERROR_NO_MUTANTE);
    }
    
    @Override
    public StatsResponseDTO stats() {
        Integer mutantDna = adnRepository.countAdnRecordsByResultEquals(true);
        Integer humanDna = adnRepository.countAdnRecordsByResultEquals(false);
        return StatsResponseDTO.builder()
                .count_mutant_dna(mutantDna)
                .count_human_dna(humanDna)
                .ratio(mutantDna.doubleValue()/humanDna.doubleValue())
                .build();
    }
    
    private void saveRecord(AdnRecord adnRecord) {
        try {
            adnRepository.save(adnRecord);
        } catch (DuplicateKeyException ex) {
            throw new MCRuntimeException(new MCError(
                    HttpStatus.BAD_REQUEST, new ErrorDTO(
                    ERROR_ADN_ALREADY_CHECKED_CODE,
                    String.format(ERROR_ADN_ALREADY_CHECKED, adnRecord.getResult()),
                    TYPE_E
            )));
        }
    }
    
    
    private AdnRecord buildAdnRecord(String[] adnMatrix, Boolean result) throws JsonProcessingException {
        return AdnRecord.builder()
                .adnMatrix(mapper.writeValueAsString(adnMatrix))
                .result(result)
                .createdAt(new Date())
                .modifiedAt(new Date())
                .build();
    }
}
