package GedcomException;

/**
 * Gere les problèmes si un individu est fantôme
 * C'est a dire qu'il n'a pas de nom et de sexe
 */
@SuppressWarnings("serial")
public class IndividuFantomeException extends GedcomException {
	/**
	 * Gere les problèmes si un individu est fantôme
	 * C'est a dire qu'il n'a pas de nom et de sexe
	 */
    public IndividuFantomeException(String message) {
        super(message);
    }
}
