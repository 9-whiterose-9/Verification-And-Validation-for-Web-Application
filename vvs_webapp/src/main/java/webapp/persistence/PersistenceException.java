package webapp.persistence;


/**
 * The top level application exception.
 * In this simple example that is the only exception we use.
 * In a more involving example, there should be subclasses of this class.
 * Note that low level exceptions (like SQLExceptions) are wrapped in this
 * exception. 
 * 
 * @author fmartins
 *
 */
public class PersistenceException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public PersistenceException(String message) {
		super (message);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public PersistenceException(String message, Exception e) {
		super (message, e);
	}

}
