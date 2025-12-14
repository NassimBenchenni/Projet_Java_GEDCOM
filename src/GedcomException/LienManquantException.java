package GedcomException;

/**
 * Gere les problèmes si le père ne connait pas ses enfants
 */
@SuppressWarnings("serial")
public class LienManquantException extends GedcomException {
	/**
	 * Gere les problèmes si le père ne connait pas ses enfants
	 */
    public LienManquantException(String message) {
        super(message);
    }
}
