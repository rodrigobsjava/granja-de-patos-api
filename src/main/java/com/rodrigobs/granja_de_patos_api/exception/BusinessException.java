package com.rodrigobs.granja_de_patos_api.exception;

public class BusinessException extends RuntimeException {
	public BusinessException(String message) {
		super(message);
	}
}