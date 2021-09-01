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
import java.util.concurrent.atomic.AtomicReference;

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
    private static final String REGEX = "[ATCG]+";
    
    DnaServiceImpl(AdnRepository adnRepository){
        this.adnRepository = adnRepository;
    }

    @Override
    public boolean isMutant(String[] dna) throws JsonProcessingException {
        List<Boolean> resultList = new ArrayList<>();
        
        // El tamano minimo de la matriz debe ser 4x4 para poder cumplir con los parametros de minimo 4 letras iguales
        if(dna.length < 4) {
            throw new MCRuntimeException(new MCError(
                    HttpStatus.BAD_REQUEST, new ErrorDTO(
                    ERROR_TAMANO_MINIMO_CODE,
                    ERROR_TAMANO_MINIMO,
                    TYPE_E
            )));
        }
        
        for (int i = 0; i < dna.length; i++) {
            
            // Se valida que la fila solo tenga los caracteres permitidos (A,T,C,G)
            if (!dna[i].matches(REGEX)){
                throw new MCRuntimeException(new MCError(
                        HttpStatus.BAD_REQUEST, new ErrorDTO(
                        ERROR_SECUENCIA_ADN_CODE,
                        String.format(ERROR_SECUENCIA_ADN, dna[i]),
                        TYPE_E
                )));
            }

            int length = dna[i].length();
            for (int j = 0; j < length; j++) {
                // Si se cumple el requerimiento de minimo dos dos filas con 4 letras igual, se retorna el resultado positivo
                if (resultList.size() > 1) {
                    saveRecord(buildAdnRecord(dna, true));
                    return true;
                }
                
                // Para hacer la validacion de igualdad horizontal, se asegura que la posicion actual no sea la ultima posicion de la fila
                // Se valida que la posicion de la derecha sea igual a la actual
                if ((j != length - 1) && (dna[i].charAt(j) == dna[i].charAt(j + 1))) {
                    
                    // Se valida que al menos existan tres posiciones mas a la derecha
                    // Se decide dejar las siguiente validaciones separadas por legibilidad del codigo
                    if(j + 2 < length - 1) {
                        
                        // Se completa la validacion horizontal hacia la derecha de 4 letras igual. (la actual, la siguiente y dos mas)
                        if ((dna[i].charAt(j) == dna[i].charAt(j + 2)) && (dna[i].charAt(j) == dna[i].charAt(j + 3))){
                                resultList.add(true);
                                log.info(String.format("HORIZONTAL: %s == %s", dna[i].charAt(j), dna[i].charAt(j + 1)));
                            }
                    }
                    
                    
                }
    
                // Para hacer la validacion de igualdad vertical, se asegura que la posicion actual no sea la ultima fila
                // Se valida que la siguiente posicion hacia abajo sea igual a la actual
                if ((i != length - 1) && (dna[i].charAt(j) == dna[i + 1].charAt(j))) {
    
                    // Se valida que al menos existan tres filas mas hacia abajo
                    // Se decide dejar las siguiente validaciones separadas por legibilidad del codigo
                    if (i + 2 < length - 1) {
    
                        // Se completa la validacion vertical hacia abajo de 4 letras igual. (la actual, la siguiente y dos mas)
                        if ((dna[i].charAt(j) == dna[i + 2].charAt(j)) && (dna[i].charAt(j) == dna[i + 3].charAt(j))) {
                            resultList.add(true);
                            log.info(String.format("VERTICAL: %s == %s", dna[i].charAt(j), dna[i + 1].charAt(j)));
                        }
                    }
                    
                }
    
                // Para hacer las validaciones de igualdad oblicuas, se asegura que la posicion actual no sea la ultima fila ni la ultima posicion de la fila
                if ((j != length - 1) && (i != length - 1)) {
                    
                    // Se valida que existan al menos tres posiciones mas oblicuas hacia la derecha
                    if ((i + 2 < length - 1) && (j + 2 < length - 1)) {
                        
                        //Se valida que existan cuatro letras iguales a la posicion actual de forma oblicua a la derecha
                        if (
                                (dna[i].charAt(j) == dna[i + 1].charAt(j + 1)) &&
                                (dna[i].charAt(j) == dna[i + 2].charAt(j + 2)) &&
                                        (dna[i].charAt(j) == dna[i + 3].charAt(j + 3))
                        ) {
                            resultList.add(true);
                            log.info(String.format("OBLICUO POSITIVO: %s == %s", dna[i].charAt(j), dna[i + 1].charAt(j + 1)));
                        }
                    }
                }
    
                // Se valida que no sea la primera posicion de la fila ni la ultima fila
                if ((j != 0) && (i != length - 1)) {
                    
                    //Se valida que existan al menos tres posiciones mas oblicuas hacia la izquierda
                    if ((i + 2 < length - 1) && (j - 2 >= 1)) {
        
                        //Se valida que existan cuatro letras iguales a la posicion actual de forma oblicua a la izquierda
                        if (
                                (dna[i].charAt(j) == dna[i + 1].charAt(j - 1)) &&
                                        (dna[i].charAt(j) == dna[i + 2].charAt(j - 2)) &&
                                        (dna[i].charAt(j) == dna[i + 3].charAt(j - 3))
                        ) {
                            resultList.add(true);
                            log.info(String.format("OBLICUO NEGATIVO: %s == %s", dna[i].charAt(j), dna[i + 1].charAt(j - 1)));
                        }
                    }
                }
                
            }
        }
    
        // Se almacena el registro de forma negativa y se responde con una excepcion para indicar que el ADN es humano
        saveRecord(buildAdnRecord(dna, false));
        throw new NoMutantException(ERROR_NO_MUTANTE);
    }
    
    @Override
    public StatsResponseDTO stats() {
        AtomicReference<Integer> mutantDna = new AtomicReference<>(0);
        AtomicReference<Integer> humanDna = new AtomicReference<>(0);
        
        // Se hace un solo llamado a la BD y se valida registro por registro
        // se hace para evitar que en momentos de alta demanda se hagan muchas peticiones a al BD
        adnRepository.findAll().stream().forEach(adnChain -> {
            if (Boolean.TRUE.equals(adnChain.getResult())){
                mutantDna.updateAndGet(v -> v + 1);
            } else if (Boolean.FALSE.equals(!adnChain.getResult())) {
                humanDna.updateAndGet(v -> v + 1);
            }
        });
        
        return StatsResponseDTO.builder()
                .count_mutant_dna(mutantDna.get())
                .count_human_dna(humanDna.get())
                .ratio(mutantDna.get().doubleValue()/ humanDna.get().doubleValue())
                .build();
    }
    
    private void saveRecord(AdnRecord adnRecord) {
        // Se guarda la entidad y si ya existe se deja el log, pero el usuario recibe una respuesta acorde con al validacion
        try {
            adnRepository.save(adnRecord);
        } catch (DuplicateKeyException ex) {
            log.warn(String.format(ERROR_ADN_ALREADY_CHECKED, adnRecord.getResult()));
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
