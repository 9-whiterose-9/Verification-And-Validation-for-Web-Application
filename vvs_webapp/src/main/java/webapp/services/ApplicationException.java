package webapp.services;


/**
 * The top level application exception.
 * 
 * In this simple example there is only one exception.
 * In a more involving example, there should be sub-classes of this class.
 * Note that low-level exceptions (like RecordNotFoundException) are wrapped 
 * in this exception. 
 * 
 * @author fmartins
 * @version 1.1 (4/10/2014)
 *
 */
public class ApplicationException extends Exception {

	/**
	 * The serial version id (generated automatically by Eclipse)
	 */
	private static final long serialVersionUID = -3416001628323171383L;

	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public ApplicationException(String message) {
		super (message);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public ApplicationException(String message, Exception e) {
		super (message, e);
	}

}
