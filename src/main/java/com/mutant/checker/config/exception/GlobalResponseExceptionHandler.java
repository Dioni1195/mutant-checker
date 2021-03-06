package com.mutant.checker.config.exception;

import com.mutant.checker.service.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;

import static com.mutant.checker.config.exception.errorcodes.ServiceErrorCodes.TYPE_E;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author <a>Dionisio Arango</a>
 * @project mutant-checker
 * @class GlobalResponseExceptionHandler
 * @date 28/08/2021
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleBindException(
		BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		MCError error = new MCError(BAD_REQUEST, buildValidationErrors(ex.getBindingResult().getFieldErrors().get(0)));
		log.error(ex.getLocalizedMessage());
		return buildResponseEntity(error);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		MCError error = new MCError(BAD_REQUEST, buildValidationErrors(ex.getBindingResult().getFieldErrors().get(0)));
		log.error(ex.getLocalizedMessage());
		return buildResponseEntity(error);
	}

	@ExceptionHandler(MCRuntimeException.class)
	protected ResponseEntity<Object> handleMCRuntimeException(MCRuntimeException ex) {
		log.error(ex.getMCError().getErrorDTO().getDescError());
		return buildResponseEntity(ex.getMCError());
	}
	
	@ExceptionHandler(NoMutantException.class)
	protected ResponseEntity<Object> handleNoMutantException(NoMutantException ex) {
		log.error(ex.getLocalizedMessage());
		return new ResponseEntity<>(ex.getNoMutant(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleError(Exception ex) {
		log.error(ex.getLocalizedMessage());
		MCError error = new MCError(INTERNAL_SERVER_ERROR, new ErrorDTO(104001, ex.getLocalizedMessage(), TYPE_E));
		return buildResponseEntity(error);
	}

	private ResponseEntity<Object> buildResponseEntity(MCError error) {
		return new ResponseEntity<>(error.getErrorDTO(), error.getStatus());
	}

	private ErrorDTO buildValidationErrors(FieldError error) {
		return new ErrorDTO(101003,
			MessageFormat.format("Object Name: {0}, Field: {1}, Message: {2}", error.getObjectName(), error.getField(), error.getDefaultMessage()),
			TYPE_E);
	}
}
