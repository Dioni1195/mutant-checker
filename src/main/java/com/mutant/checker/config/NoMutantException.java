package com.mutant.checker.config;

public class NoMutantException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private final Boolean noMutant;
	
	public NoMutantException(String message) {
		super(message);
		this.noMutant = false;
	}
	
	public Boolean getNoMutant() {
		return noMutant;
	}
}
