package it.ifttt.model.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class UnauthorizedChannelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedChannelException(String message) {
		super("Unauthorized Channel: " + message);
	}
}