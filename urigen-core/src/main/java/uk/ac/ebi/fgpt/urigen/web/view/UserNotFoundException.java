package uk.ac.ebi.fgpt.urigen.web.view;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Simon Jupp
 * @date 12/12/2016
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="User Not Found") //404
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public UserNotFoundException(){
		super("User not found withb that email");
	}
}