import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecteurGedcom {

    private Genealogie genealogie;

    public LecteurGedcom(Genealogie genealogie) {
        this.genealogie = genealogie;
    }

    public void lireFichier(String cheminFichier) {
        System.out.println("Lecture du fichier : " + cheminFichier);

        // Variables temp
        Individu individuCourant = null;
        Famille familleCourante = null;

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {	
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;

                // On découpe la ligne en 3 morceaux max : Niveau / Tag / Reste
                // Ex: "1 NAME John /Doe/" donne ["1", "NAME", "John /Doe/"]
                String[] morceaux = ligne.split(" ", 3);
                int niveau = Integer.parseInt(morceaux[0]);
                
                // Gestion des ID qui sont parfois en 2ème position (ex: 0 @I1@ INDI)
                String tag;
                String valeur = "";
                
                if (morceaux.length > 2 && morceaux[1].startsWith("@")) {
                    // Cas spécial niveau 0 : 0 @ID@ TAG
                    tag = morceaux[2]; 
                    valeur = morceaux[1]; // L'ID est ici
                } else {
                    // Cas classique : 1 TAG VALEUR
                    tag = morceaux[1];
                    if (morceaux.length > 2) valeur = morceaux[2];
                }

                // --- TRAITEMENT DES LIGNES ---

                if (niveau == 0) {
                    // Nouveau contexte : on remet tout à zéro
                    individuCourant = null;
                    familleCourante = null;

                    switch (tag) {
	                    case "HEAD":
	                        System.out.println("--- DÉBUT DU FICHIER GEDCOM ---");
	                        break;
	                        
	                    case "TRLR":
	                        System.out.println("--- FIN DU FICHIER ---");
	                        break;
	
	                    case "INDI":
	                        individuCourant = genealogie.getOrCreateIndividu(valeur);
	                        break;
	
	                    case "FAM":
	                        familleCourante = genealogie.getOrCreateFamille(valeur);
	                        break;
	                        
	                    default:
	                        System.out.println("Note : Tag niveau 0 inconnu " + tag);
                }
            }
                else if (niveau == 1) {
                    // On remplit l'objet (Individu OU Famille)
                    
                    if (individuCourant != null) {
                        switch (tag) {
                            case "NAME": individuCourant.setNom(valeur); break;
                            case "SEX":  individuCourant.setSexe(valeur); break;
                            case "FAMC": 
                                // L'individu est enfant dans cette famille
                                Famille parents = genealogie.getOrCreateFamille(valeur);
                                individuCourant.setFamc(parents);
                                break;
                            case "FAMS":
                                // L'individu est parent dans cette famille
                                Famille foyer = genealogie.getOrCreateFamille(valeur);
                                individuCourant.ajouterFams(foyer);
                                break;
                        }
                    } 
                    else if (familleCourante != null) {
                        switch (tag) {
                            case "HUSB":
                                // Le mari est...
                                Individu mari = genealogie.getOrCreateIndividu(valeur);
                                familleCourante.setPere(mari);
                                break;
                            case "WIFE":
                                // La femme est...
                                Individu femme = genealogie.getOrCreateIndividu(valeur);
                                familleCourante.setMere(femme);
                                break;
                            case "CHIL":
                                // Un enfant est...
                                Individu enfant = genealogie.getOrCreateIndividu(valeur);
                                familleCourante.ajouterEnfant(enfant);
                                break;
                        }
                    }
                }
            }
            System.out.println("Lecture terminée !");
            
        } catch (IOException e) {
            System.err.println("Erreur lecture fichier : " + e.getMessage());
        } catch (NumberFormatException e) {
            // Ignore les lignes qui commence pas par des chiffres
        }
    }
}