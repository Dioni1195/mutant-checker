package com.mutant.checker.config;

public class MCRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final transient MCError mcError;
	
	public MCRuntimeException(MCError mcError) {
		this.mcError = mcError;
	}
	
	public MCRuntimeException(String message, Throwable cause, MCError mcError) {
		super(message, cause);
		this.mcError = mcError;
	}
	
	public MCError getMCError() {
		return mcError;
	}
}
