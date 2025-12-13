import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;
import GedcomException.LienManquantException;

@SuppressWarnings("serial")
public class Genealogie implements Serializable{

    private Map<String, Individu> mapIndividus = new HashMap<>(); 
    private Map<String, Famille> mapFamilles = new HashMap<>();

    //  La méthode pour récupérer/créer une famille
    public Famille getOrCreateFamille(String idCherche) {
        if (mapFamilles.containsKey(idCherche)) {
        	// si il existe on le return
            return mapFamilles.get(idCherche);
        } else {
        	// sinon il n'existe pas.
            // On crée un individu (incomplet)
            Famille fantome = new Famille(idCherche);
            mapFamilles.put(idCherche, fantome);
            return fantome;
        }
    }
    
    // pour individu
    public Individu getOrCreateIndividu(String idCherche) {
        
        if (mapIndividus.containsKey(idCherche)) {
        	// si il existe on le return
            return mapIndividus.get(idCherche);
        } 
        else {
            // sinon il n'existe pas.
            // On crée un individu (incomplet)
            Individu fantome = new Individu(idCherche);
            // On le range
            mapIndividus.put(idCherche, fantome);
            
            return fantome;
        }
    }
    
	public void validerDonnees() {
	    System.out.println("Début de l'inspection...");

	    // On parcourt tous les individus pour vérifier la réciprocité
	    for (Individu ind : mapIndividus.values()) {
	        
	        // Vérification Parents -> Enfant
	        if (ind.getFamc() != null) {
	            Famille familleParents = ind.getFamc();
	            
	            // Si la famille ne contient pas cet enfant dans sa liste
	            if (!familleParents.getEnfants().contains(ind)) {
	                try {
	                    // Erreur
	                    throw new LienManquantException("L'individu " + ind.getNom() + " connait ses parents, mais eux ne le connaissent pas !");
	                } catch (LienManquantException e) {
	                    System.err.println("Erreur : " + e.getMessage());
	                    System.out.println("Correction automatique en cours...");
	                    // on ajoute ducoup
	                    familleParents.ajouterEnfant(ind);
	                }
	            }
	        }
	    }
	}
	
	public Individu rechercherParNom(String prenomCherche, String nomCherche) {
	    
	    // 1. On parcourt tous les individus de la base
	    for (Individu ind : mapIndividus.values()) {
	        
	        // On récupère le nom complet de l'individu (ex: "John /Begood/")
	        String nomComplet = ind.getNom(); 
	        
	        if (nomComplet != null) {
	            // 2. On vérifie si ce nom contient ce qu'on cherche
	            if (nomComplet.contains(nomCherche) && nomComplet.contains(prenomCherche)) {
	                // renvoie l'individu
	                return ind; 
	            }
	        }
	    }
	    
	    // 3. Si on a tout parcouru et rien trouvé
	    return null;
	}
}
	

	
	

