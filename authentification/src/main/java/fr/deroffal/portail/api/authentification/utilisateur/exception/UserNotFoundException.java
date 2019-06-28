package fr.deroffal.portail.api.authentification.utilisateur.exception;

import fr.deroffal.portail.api.exception.PortailRestException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFoundException extends PortailRestException {

	private final String login;

	public UserNotFoundException(final String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	@Override
	public String getMessageClient() {
		return "Utilisateur non-existant : " + getLogin();
	}

	@Override
	public HttpStatus getHttpStatus() {
		return NOT_FOUND;
	}
}