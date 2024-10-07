package br.com.cadastroit.services.exceptions;

public class EstadoException extends GenericException {

	private static final long serialVersionUID = 8301404284490916435L;

	public EstadoException(String message) {

		super(message);
	}

	public EstadoException(String message, Throwable cause) {

		super(buildMessage(message, cause), cause);
	}
}
