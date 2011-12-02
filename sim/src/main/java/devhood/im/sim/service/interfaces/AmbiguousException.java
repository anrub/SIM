package devhood.im.sim.service.interfaces;

/**
 * Exception wird geworfen, falls der Username bereits vorhanden ist oder sonst
 * eine uneindeutdigkeit vorkommt.
 * 
 * @author flo
 * 
 */
public class AmbiguousException extends Exception {
	public AmbiguousException(String msg) {
		super(msg);
	}

	public AmbiguousException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
