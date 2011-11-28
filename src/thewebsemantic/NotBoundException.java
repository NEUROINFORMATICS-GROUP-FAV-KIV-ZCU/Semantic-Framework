package thewebsemantic;

/**
 * This class represents the exception that is thrown when the loaded
 * class can not be readen by reflection.
 * 
 */
public class NotBoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotBoundException(String msg) {
		super(msg);
	}
}
