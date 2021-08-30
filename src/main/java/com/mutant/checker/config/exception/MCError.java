package com.mutant.checker.config.exception;

import com.mutant.checker.service.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MCError implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private HttpStatus status;
	private ErrorDTO errorDTO;
}
