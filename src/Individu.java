import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tagGedcom.TagNom;
import tagGedcom.TagSexe;

@SuppressWarnings("serial")
public class Individu implements Serializable{

	private TagNom nom;    
    private TagSexe sexe;
	private String id;
	private Famille famc;
	private List<Famille> fams;
	
	public Individu(String id) {
		this.id = id;
		this.fams = new ArrayList<>();
	}

	public List<Individu> getTousLesEnfants() {
	    List<Individu> resultat = new ArrayList<>();
	    
	    // Pour chacune de mes familles où je suis parent
	    for (Famille fam : this.fams) {
	        // J'ajoute tous les enfants de cette famille à ma liste
	        resultat.addAll(fam.getEnfants());
	    }
	    
	    return resultat;
	}
	
	public List<Individu> getFreresEtSoeurs() {
	    // Si je n'ai pas de parents connus, je n'ai pas de frères/sœurs connus
	    if (this.famc == null) {
	        return new ArrayList<>(); // Liste vide
	    }

	    // Je récupère tous les enfants de mes parents
	    List<Individu> tous = this.famc.getEnfants();
	    List<Individu> fratrie = new ArrayList<>();

	    for (Individu enfant : tous) {
    		// j'ajoute les freres et soeurs
	        if (enfant != this) {
	            fratrie.add(enfant);
	        }
	    }
	    
	    return fratrie;
	}
	
	public boolean estMarieAvec(Individu autre) {
	    // Je regarde toutes mes familles
	    for (Famille fam : this.fams) {
	        // mari et femme de la famille
	        Individu mari = fam.getPere();
	        Individu femme = fam.getMere();
	        
	        // Si l'autre est mon mari OU ma femme dans cette famille
	        if (mari == autre || femme == autre) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public String getId() { return id; }

    public void setNom(TagNom nom) { this.nom = nom; }
	
    public void setNom(String valeurNom) {
        this.nom = new TagNom(valeurNom);
    }
	
    
	public void setSexe(String valeurSexe) {
        this.sexe = new TagSexe(valeurSexe);
    }
    
	public String getSexe() { 
        if (this.sexe != null) return this.sexe.getValeur();
        return null; 
    }
    
    public TagNom getTagNom() {
        return nom;
    }
    
    public String getNom() { return getNomString(); }

    public String getNomString() {
        if (this.nom != null) return this.nom.getValeur();
        return "Inconnu";
    }
    
 // Pour les FAMILLES 
    public Famille getFamc() { return famc; }
    public void setFamc(Famille famc) { this.famc = famc; }

    public List<Famille> getFams() { return fams; }
    
    // Méthode pour ajouter une famille FAMS
    public void ajouterFams(Famille famille) {
        if (!this.fams.contains(famille)) {
            this.fams.add(famille);
        }
    }
    
    @Override
    public String toString() {
        return getNomString() + " (" + id + ")";
    }
}
