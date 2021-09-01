package com.mutant.checker.controller.custom_constraints;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class SquareMatrixValidator implements ConstraintValidator<SquareMatrix, String[]> {
	@Override
	public void initialize(SquareMatrix constraintAnnotation) {
		log.info("Validacion matriz cuadrada");
	}
	
	@Override
	public boolean isValid(String[] value, ConstraintValidatorContext context) {
		
		for (String adnChain: value) {
			if (adnChain.length() != value.length)
				return false;
		}
		return true;
	}
}
