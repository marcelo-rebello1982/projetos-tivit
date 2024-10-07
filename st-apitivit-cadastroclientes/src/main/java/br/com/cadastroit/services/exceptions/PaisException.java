package br.com.cadastroit.services.exceptions;

public class PaisException extends GenericException {

	private static final long serialVersionUID = -7002196773653709805L;

	public PaisException(String message) {

		super(message);
	}

	public PaisException(String message, Throwable cause) {

		super(buildMessage(message, cause), cause);
	}
}
