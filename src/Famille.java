import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet de gérer une Famille
 * Elle contient les attributs : id, pere, mere, et ses enfants dans une liste
 */
@SuppressWarnings("serial")
public class Famille implements Serializable {
	
	private String id;
	private Individu pere;
	private Individu mere;
	private List<Individu> enfants;
	
	

	public Famille(String id) {
		super();
		this.id = id;
		this.enfants = new ArrayList<>();
	}
	
	public void ajouterEnfant(Individu enfant) {
        if (!enfants.contains(enfant)) {
            enfants.add(enfant);
        }
    }
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Individu getPere() {
		return pere;
	}
	public void setPere(Individu pere) {
		this.pere = pere;
	}
	public Individu getMere() {
		return mere;
	}
	public void setMere(Individu mere) {
		this.mere = mere;
	}
	public List<Individu> getEnfants() {
		return enfants;
	}
	
}
