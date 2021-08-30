package com.mutant.checker.service.dto;

import com.mutant.checker.controller.customConstraints.SquareMatrix;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDnaRequestDTO {
    @NotNull
    @NotEmpty
    @SquareMatrix
    private String[] dna;
}
