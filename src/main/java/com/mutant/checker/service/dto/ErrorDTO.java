package com.mutant.checker.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class ErrorDTO
 * @date 26/08/2021
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idError;
    private String descError;
    private String tipoError;
}
