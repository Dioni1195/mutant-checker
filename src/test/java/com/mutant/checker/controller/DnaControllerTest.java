package com.mutant.checker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutant.checker.service.DnaService;
import com.mutant.checker.service.dto.CheckDnaRequestDTO;
import com.mutant.checker.service.dto.StatsResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DnaController.class)
public class DnaControllerTest {
	
	public static final ObjectMapper mapper = new ObjectMapper();
	private static final String DNA_URL = "/mutant/";
	private static final String STATS_URL = "/mutant/stats";
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	DnaService dnaService;
	
	@Test
	void checkDnaShouldReturnOk() throws Exception {
		CheckDnaRequestDTO request = buildRequestCheckDna(new String[]{
			"ATCGCA",
			"CAGTAG",
			"TTTGGG",
			"AGGTGG",
			"CGCCTG",
			"ACAAAA"
		});
		when(dnaService.isMutant(request.getDna())).thenReturn(true);
		
		mockMvc.perform(post(DNA_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(request))
		)
			.andExpect(status().isOk())
			.andReturn();
	}
	
	@Test
	void checkDnaErrorDnaNull() throws Exception {
		mockMvc.perform(post(DNA_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(CheckDnaRequestDTO.builder().build()))
		)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.idError").value("101003"))
			.andExpect(jsonPath("$.descError").value("Object Name: checkDnaRequestDTO, Field: dna, Message: must not be empty"))
			.andReturn();
	}
	
	@Test
	void checkDnaErrorDnaEmpty() throws Exception {
		mockMvc.perform(post(DNA_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(buildRequestCheckDna(new String[]{})))
		)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.idError").value("101003"))
			.andExpect(jsonPath("$.descError").value("Object Name: checkDnaRequestDTO, Field: dna, Message: must not be empty"))
			.andReturn();
	}
	
	@Test
	void statsShouldReturnOk() throws Exception {
		when(dnaService.stats()).thenReturn(buildResponseStats(40, 100));
		
		mockMvc.perform(get(STATS_URL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.ratio").value("0.4"))
			.andReturn();
	}
	
	@Test
	void checkDnaErrorNotSquareMatrix() throws Exception {
		CheckDnaRequestDTO request = buildRequestCheckDna(new String[]{
			"ATCGCA",
			"CAGTAG",
			"TTTGGG",
			"AGGTGG",
			"CGCCTG",
			"ACAAA"
		});
		
		mockMvc.perform(post(DNA_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(request))
		)
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.idError").value("101003"))
			.andExpect(jsonPath("$.descError").value("Object Name: checkDnaRequestDTO, Field: dna, Message: La matriz debe ser cuadrada"))
			.andReturn();
	}
	
	CheckDnaRequestDTO buildRequestCheckDna(String[] dna){
		return CheckDnaRequestDTO.builder()
			.dna(dna)
			.build();
	}
	
	StatsResponseDTO buildResponseStats(Integer mutant, Integer human) {
		return StatsResponseDTO.builder()
			.count_mutant_dna(mutant)
			.count_human_dna(human)
			.ratio(mutant.doubleValue()/human.doubleValue())
			.build();
	}
}
