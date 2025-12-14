package GedcomException;

/**
 * Cas où l'individu est ancêtre de lui-même
 */
@SuppressWarnings("serial")
public class CycleException extends GedcomException {
	/**
	 * Cas où l'individu est ancêtre de lui-même
	 */
	public CycleException(String message) {
		super(message);
	}
}