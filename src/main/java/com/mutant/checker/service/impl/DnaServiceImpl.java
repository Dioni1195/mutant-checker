package com.mutant.checker.service.impl;

import com.mutant.checker.config.MCError;
import com.mutant.checker.config.MCRuntimeException;
import com.mutant.checker.config.NoMutantException;
import com.mutant.checker.service.DnaService;
import com.mutant.checker.service.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public boolean isMutant(String[] dna) {
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
        
        throw new NoMutantException(ERROR_NO_MUTANTE);
    }
}
