package com.mutant.checker.controller.customConstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.mutant.checker.config.exception.errorcodes.ServiceErrorCodes.ERROR_MATRIZ_NO_CUADRADA;
import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = { SquareMatrixValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SquareMatrix {
	String message() default ERROR_MATRIZ_NO_CUADRADA;
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
