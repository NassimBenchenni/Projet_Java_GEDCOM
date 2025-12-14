package GedcomException;

@SuppressWarnings("serial")
public class IncoherenceSexeException extends GedcomException {
	/**
	 * Gere le cas ou le mari n'est pas un homme est inversement
	 */
    public IncoherenceSexeException(String message) {
        super(message);
    }
}