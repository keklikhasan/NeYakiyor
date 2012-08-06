package com.minikod.exception;
/**
 * 
 * @author hkeklik
 *
 */
public class DaoServiceException extends Exception {

	private static final long serialVersionUID = -6883984142339657062L;

	public DaoServiceException() {

	}

	public DaoServiceException(String message) {
		super(message);
	}

	public DaoServiceException(Throwable exception) {
		super(exception);
	}
}
