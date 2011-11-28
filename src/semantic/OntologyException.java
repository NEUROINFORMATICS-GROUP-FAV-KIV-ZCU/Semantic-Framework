package semantic;

/**
 * This exception indicates, that there occurred problems while processing ontology
 * by the OWL-API ontology manager.
 * This exception informs user that there occurred an error during the transformation
 * process, concrete errors are described in logger.
 * 
 * @author Jakub Krauz
 */
public class OntologyException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an OntologyException object initialized with the given Throwable
	 * object.
	 */
	public OntologyException(Throwable cause) {
		super(cause.toString());
		initCause(cause);
	}

}
