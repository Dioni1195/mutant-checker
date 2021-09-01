package com.mutant.checker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutant.checker.AdnRepository;
import com.mutant.checker.config.exception.MCRuntimeException;
import com.mutant.checker.config.exception.NoMutantException;
import com.mutant.checker.service.dto.AdnRecord;
import com.mutant.checker.service.dto.StatsResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mutant.checker.config.exception.errorcodes.ServiceErrorCodes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DnaServiceImplTest {
	public static final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	DnaService dnaService;
	
	@MockBean
	AdnRepository adnRepository;
	
	@Test
	void checkDnaOblicuoPositivo() throws JsonProcessingException {
		String[] request = new String[]{
			"ACCGCA",
			"CACTAG",
			"TTACGG",
			"AGGACC",
			"CGCCTG",
			"ACTGAA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	@Test
	void checkDnaCoincidencePoint() throws JsonProcessingException {
		String[] request = new String[]{
			"AAAA",
			"CACA",
			"TTAA",
			"AGGA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	@Test
	void checkFullMutant() throws JsonProcessingException {
		String[] request = new String[]{
			"AAAA",
			"AAAA",
			"AAAA",
			"AAAA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	@Test
	void checkAdnAllAtSameTime() throws JsonProcessingException {
		String[] request = new String[]{
			"ATAATCGA",
			"AAAGCGGA",
			"GGCTGGGT",
			"CATAAAAG",
			"ATAAACGA",
			"GAGACAGA",
			"ACCAGGAT",
			"ATACTTTG"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	@Test
	void statsShouldOk() throws JsonProcessingException {
		when(adnRepository.findAll()).thenReturn(buildFindAllResponse());
		
		StatsResponseDTO result = dnaService.stats();
		
		assertEquals(2.0, result.getRatio());
		assertEquals(4, result.getCount_mutant_dna());
		assertEquals(2, result.getCount_human_dna());
	}
	
	@Test
	void checkDnaErrorSizeMatrix() throws JsonProcessingException {
		String[] request = new String[]{
			"ATCGCA",
			"CAGTAG"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		MCRuntimeException exception = assertThrows(MCRuntimeException.class, () ->
			dnaService.isMutant(request));
		
		assertEquals(HttpStatus.BAD_REQUEST, exception.getMCError().getStatus());
		assertEquals(ERROR_TAMANO_MINIMO, exception.getMCError().getErrorDTO().getDescError());
		assertEquals(ERROR_TAMANO_MINIMO_CODE, exception.getMCError().getErrorDTO().getIdError());
		assertEquals(TYPE_E, exception.getMCError().getErrorDTO().getTipoError());
	}
	
	@Test
	void checkDnaErrorInvalidCharacter() throws JsonProcessingException {
		String[] request = new String[]{
			"ATCWCA",
			"CAGTAG",
			"TTTGGG",
			"AGGTGG",
			"CGCCTG",
			"ACAAAA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		MCRuntimeException exception = assertThrows(MCRuntimeException.class, () ->
			dnaService.isMutant(request));
		
		assertEquals(HttpStatus.BAD_REQUEST, exception.getMCError().getStatus());
		assertEquals(String.format(ERROR_SECUENCIA_ADN, "ATCWCA"), exception.getMCError().getErrorDTO().getDescError());
		assertEquals(ERROR_SECUENCIA_ADN_CODE, exception.getMCError().getErrorDTO().getIdError());
		assertEquals(TYPE_E, exception.getMCError().getErrorDTO().getTipoError());
	}
	
	@Test
	void checkDnaValidStringInBoundaries() throws JsonProcessingException {
		// prueba para validar que si las cadenas estan en la ultima columna y la ultima fila, se validen correctamente
		String[] request = new String[]{
			"ATCGCA",
			"CAGTAG",
			"TTTGCG",
			"AGGTAG",
			"CGCCTG",
			"ACAAAA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	@Test
	void checkDnaHuman() throws JsonProcessingException {
		String[] request = new String[]{
			"ATCGCA",
			"CAGTAT",
			"TTTGCG",
			"AGGTAG",
			"CGCCTG",
			"ACAATA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		NoMutantException exception = assertThrows(NoMutantException.class, () ->
			dnaService.isMutant(request));
		
		assertFalse(exception.getNoMutant());
	}
	
	@Test
	void checkDnaErrorSave() throws JsonProcessingException {
		String[] request = new String[]{
			"ATCGCA",
			"CAGTAG",
			"TTTGGG",
			"AGGTGG",
			"CGCCTG",
			"ACAAAA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenThrow(new DuplicateKeyException("TEST"));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	@Test
	void checkDnaOblicuoNegativo() throws JsonProcessingException {
		String[] request = new String[]{
			"ATCGCA",
			"CAGTAG",
			"TTTAGC",
			"AGATGG",
			"CGCCTG",
			"ACAAAA"
		};
		when(adnRepository.save(any(AdnRecord.class))).thenReturn(buildAdnRecord(true, request));
		
		boolean result = dnaService.isMutant(request);
		
		assertTrue(result);
	}
	
	private List<AdnRecord> buildFindAllResponse() throws JsonProcessingException {
		List<AdnRecord> listAdn = new ArrayList<>();
		
		listAdn.add(buildAdnRecord(true, new String[]{"TEST1"}));
		listAdn.add(buildAdnRecord(true, new String[]{"TEST2"}));
		listAdn.add(buildAdnRecord(true, new String[]{"TEST3"}));
		listAdn.add(buildAdnRecord(true, new String[]{"TEST4"}));
		listAdn.add(buildAdnRecord(false, new String[]{"TEST5"}));
		listAdn.add(buildAdnRecord(false, new String[]{"TEST6"}));
		
		return listAdn;
	}
	
	AdnRecord buildAdnRecord(Boolean result, String[] adn) throws JsonProcessingException {
		return AdnRecord.builder()
			.modifiedAt(new Date())
			.createdAt(new Date())
			.result(result)
			.adnMatrix(mapper.writeValueAsString(adn))
			.build();
	}
}
