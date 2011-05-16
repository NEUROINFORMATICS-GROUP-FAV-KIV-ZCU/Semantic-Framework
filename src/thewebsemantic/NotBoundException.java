package thewebsemantic;

/**
 * This class represents the exception that is thrown when the loaded
 * class can not be readen by reflection.
 * 
 */
public class NotBoundException extends RuntimeException {
	public NotBoundException(String msg) {
		super(msg);
	}
}
