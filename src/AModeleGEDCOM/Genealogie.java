package AModeleGEDCOM;
import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import GedcomException.CycleException;
import GedcomException.IncoherenceSexeException;
import GedcomException.IndividuFantomeException;
import GedcomException.LienManquantException;


/**
 * Cette classe permet de gérer toutes les familles et individus de la base
 * Dans 2 dictionnaires séparés
 */
@SuppressWarnings("serial")
public class Genealogie implements Serializable{

    private Map<String, Individu> mapIndividus = new HashMap<>(); 
    private Map<String, Famille> mapFamilles = new HashMap<>();

    /**
     * On renvoie l'id d'une Famille mais
     * si elle n'existe pas encore on la créer
     */
    public Famille getOrCreateFamille(String idCherche) {
        if (mapFamilles.containsKey(idCherche)) {
        	// si il existe on le return
            return mapFamilles.get(idCherche);
        } else {
        	// sinon il n'existe pas.
            // On crée une famille
            Famille fantome = new Famille(idCherche);
            mapFamilles.put(idCherche, fantome);
            return fantome;
        }
    }
    
    /**
     * On renvoie l'id d'un indivudu mais
     * si il n'existe pas encore on le créer (mais sans nom, prenom ect..)
     */
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
    
    /**
     * Vérifie la cohérence des liens familiaux (réciprocité Parents-Enfants)
     * et corrige automatiquement les liens manquants si nécessaire.
     * Vérifie si le mari n'est pas une femme et inversement
     */
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
	        
	        //  Vérifie les cycles (si un individu n'est pas son propr ancêtre)
	        Famille famc = ind.getFamc();
			 for (Famille fams : ind.getFams()) {
				 if (famc != null && famc == fams) {
					 try {
						 throw new CycleException("L'individu" + ind.getNom()+ "l'individu est son propre ancêtre");
					 } catch (CycleException e) {
						 System.out.println("Anomalie détectée : " + e.getMessage());
					 }
				 }
			 }
			 
			 // Vérifie si l'individu est fantôme (C'est a dire qu'il n'a pas de nom et de sexe)
			 if (ind.getSexe() == null && ind.getNom() == null) {
                 try {
                     throw new IndividuFantomeException("L'individu avec l'ID " + ind.getId() + " n'a ni nom ni sexe défini !");
                 } catch (IndividuFantomeException e) {
                     System.err.println("Anomalie détectée : " + e.getMessage());
                 }
             }
	    }

	    
		 for (Famille fam : mapFamilles.values()) {
		     // Vérification du PÈRE (HUSB)
		     Individu pere = fam.getPere();
		     if (pere != null && "F".equals(pere.getSexe())) { // Si le père est une Femme
		         try {
		             throw new IncoherenceSexeException("Le père de la famille " + fam.getId() + " (" + pere.getNom() + ") est une femme !");
		         } catch (IncoherenceSexeException e) {
		             System.err.println("Anomalie détectée : " + e.getMessage());
		         }
		     }
	
		     // Vérification de la MÈRE (WIFE)
		     Individu mere = fam.getMere();
		     if (mere != null && "M".equals(mere.getSexe())) { // Si la mère est un Homme
		         try {
		             throw new IncoherenceSexeException("La mère de la famille " + fam.getId() + " (" + mere.getNom() + ") est un homme !");
		         } catch (IncoherenceSexeException e) {
		             System.err.println("Anomalie détectée : " + e.getMessage());
		         }
		     }
		 }
		
	}
	
	/**
     * Recherche un individu dans le HashMap à partir de son nom et prénom.
     * retourn null si il n'est pas trouvé
     */
	public Individu rechercherParNom(String prenomCherche, String nomCherche) {
	    
	    // 1. On parcourt tous les individus de la base
	    for (Individu ind : mapIndividus.values()) {
	        
	        // On récupère le nom complet de l'individu (nom, prénom)
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
	
	/**
     * On transforme le fichier en serialisation pour le sauvegarder
     */
	public void sauvegarderDansFichier(String nomFichier) {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
	        oos.writeObject(this); // On sauvegarde l'objet Genealogie
	        System.out.println("Sauvegarde réussie dans " + nomFichier);
	    } catch (IOException e) {
	        System.err.println("Erreur de sauvegarde : " + e.getMessage());
	    }
	}
	
	/**
     * On récupère le fichier sérialiser, (de type Genealogie cette fois ci)
     */
	public static Genealogie chargerDepuisFichier(String nomFichier) {
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
	        return (Genealogie) ois.readObject();
	    } catch (IOException | ClassNotFoundException e) {
	        System.err.println("Erreur de chargement : " + e.getMessage());
	        return null; 
	    }
	}
}
	

	
	

