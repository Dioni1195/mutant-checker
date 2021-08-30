package com.mutant.checker.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class StatsResponseDTO
 * @date 26/08/2021
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponseDTO {
	private Integer count_mutant_dna;
	private Integer count_human_dna;
	private Double ratio;
}
