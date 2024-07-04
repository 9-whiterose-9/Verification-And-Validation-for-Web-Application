package webapp.persistence;


/**
 * Record not found exception.
 * 
 * @author fmartins
 *
 */
public class RecordNotFoundException extends PersistenceException {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = 4484081634671467186L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public RecordNotFoundException(String message) {
		super (message);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public RecordNotFoundException(String message, Exception e) {
		super (message, e);
	}

}
