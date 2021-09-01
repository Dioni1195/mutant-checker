package com.mutant.checker.service.dto;

import com.mutant.checker.controller.customConstraints.FirstOrder;
import com.mutant.checker.controller.customConstraints.SecondOrder;
import com.mutant.checker.controller.customConstraints.SquareMatrix;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GroupSequence({CheckDnaRequestDTO.class, FirstOrder.class, SecondOrder.class})
public class CheckDnaRequestDTO {
    @NotNull(groups = FirstOrder.class)
    @SquareMatrix(groups = SecondOrder.class)
    private String[] dna;
}
