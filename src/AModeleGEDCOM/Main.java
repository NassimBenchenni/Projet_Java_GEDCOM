package AModeleGEDCOM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Permet de générer des informations sur les individus avec différente commande
 * commande actuelle :(INFO, CHILD, SIBLINGS, MARRIED, FAMC, EXIT, SAVE, LOAD)
 */
public class Main {
	/**
	 * Fonction qui ne prends pas d'argument
	 * Par contre une nom de fichier GEDCOM est demandé par la suite
	 */
    public static void main(String[] args) {
        
    	// Boucle d'interaction avec BufferedReader pour lire ligne par ligne
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        boolean continuer = true;
        
        // Initialisation
        Genealogie genealogie = new Genealogie();
        LecteurGedcom lecteur = new LecteurGedcom(genealogie);
        
        
        // Chargement du fichier
        System.out.println("En attente d'un fichier GEDCOM");
        System.out.print("> ");
        String fichier = null;
		try {
			fichier = console.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
        lecteur.lireFichier(fichier);
  
        // On vérifie si y pas de problème
        genealogie.validerDonnees();
        
        
        
        try {
            while (continuer) {
                System.out.println("\nEntrez une commande (INFO, CHILD, SIBLINGS, MARRIED, FAMC, EXIT, SAVE, LOAD) :");
                System.out.print("> ");
                
                // Lecture de la ligne
                String ligne = console.readLine();
                
                // Si l'utilisateur fait Ctrl+C ou que la ligne est vide
                if (ligne == null) break;
                ligne = ligne.trim();
                if (ligne.isEmpty()) continue;
    
                // On découpe la commande
                String[] mots = ligne.split(" "); 
                String commande = mots[0].toUpperCase();
                
                switch (commande) {
                    case "EXIT":
                        continuer = false;
                        break;
                        
                    case "INFO":
                        if (mots.length < 3) { System.out.println("Erreur : Il faut 'INFO Prénom Nom'"); break; }
                        // Rappel : mots[1] = Prénom, mots[2] = Nom (ex: John Begood)
                        Individu trouveInfo = genealogie.rechercherParNom(mots[1], mots[2]);
                        
                        if (trouveInfo != null) {
                            System.out.println("--- INFOS SUR " + trouveInfo.getNom() + " ---");
                            System.out.println("ID   : " + trouveInfo.getId());
                            System.out.println("Nom  : " + trouveInfo.getNom());
                            System.out.println("Sexe : " + trouveInfo.getSexe());
                            
                            // afficher les parents s'ils existent
                            if (trouveInfo.getFamc() != null) {
                                System.out.println("Enfant de la famille : " + trouveInfo.getFamc().getId());
                            }
                        } else {
                            System.out.println("Personne introuvable ! (Vérifiez les majuscules/espaces)");
                        }
                        break;
                        
                    case "CHILD":
                        if (mots.length < 3) { System.out.println("Erreur : Il faut 'CHILD Prénom Nom'"); break; }
                        Individu parent = genealogie.rechercherParNom(mots[1], mots[2]);
                        
                        if (parent != null) {
                            System.out.println("--- ENFANTS DE " + parent.getNom() + " ---");
                            // On vérifie si la liste est vide
                            if (parent.getTousLesEnfants().isEmpty()) {
                                System.out.println("(Aucun enfant trouvé)");
                            } else {
                                for (Individu enf : parent.getTousLesEnfants()) {
                                    System.out.println("- " + enf.getNom() + " (" + enf.getSexe() + ")");
                                }
                            }
                        } else {
                            System.out.println("Personne introuvable !");
                        }
                        break;
    
                    case "SIBLINGS":
                        if (mots.length < 3) { System.out.println("Erreur : Il faut 'SIBLINGS Prénom Nom'"); break; }
                        Individu frere = genealogie.rechercherParNom(mots[1], mots[2]);
                        
                        if (frere != null) {
                            System.out.println("--- FRERES ET SOEURS DE " + frere.getNom() + " ---");
                            if (frere.getFreresEtSoeurs().isEmpty()) {
                                System.out.println("(Aucun frère ou sœur trouvé)");
                            } else {
                                for (Individu s : frere.getFreresEtSoeurs()) {
                                    System.out.println("- " + s.getNom());
                                }
                            }
                        } else {
                            System.out.println("Personne introuvable !");
                        }
                        break;
                    case "SAVE":
                        // Sauvegarde l'état actuel de la mémoire
                        genealogie.sauvegarderDansFichier("sauvegarde_genealogie.ser");
                        break;

                    case "LOAD":
                        // Charge une mémoire existante
                        Genealogie chargee = Genealogie.chargerDepuisFichier("sauvegarde_genealogie.ser");
                        if (chargee != null) {
                            genealogie = chargee; // On remplace la généalogie courante par celle du fichier
                            // faut reconnecter le lecteur à la nouvelle généalogie.
                            lecteur = new LecteurGedcom(genealogie);
                            System.out.println("Généalogie chargée");
                        }
                        break;

                    case "MARRIED":
                        // vérifie
                        if (mots.length < 5) { 
                             // demande juste "MARRIED Prenom1 Nom1 Prenom2 Nom2"
                             System.out.println("Usage : MARRIED Prenom1 Nom1 Prenom2 Nom2");
                        } else {
                             Individu ind1 = genealogie.rechercherParNom(mots[1], mots[2]);
                             Individu ind2 = genealogie.rechercherParNom(mots[3], mots[4]);
                             if (ind1 != null && ind2 != null) {
                                 boolean maries = ind1.estMarieAvec(ind2);
                                 System.out.println(ind1.getNom() + " est marié avec " + ind2.getNom() + " ? " + maries);
                             } else {
                                 System.out.println("L'un des individus n'existe pas.");
                             }
                        }
                        break;
                    case "FAMC":
                        if (mots.length < 3) { System.out.println("Usage : FAMC Prénom Nom"); break; }
                        
                        // 1. On trouve la personne
                        Individu enfant = genealogie.rechercherParNom(mots[1], mots[2]);
                        
                        if (enfant != null) {
                            // 2. On récupère sa famille "FAMC" (là où il est enfant)
                            Famille fam = enfant.getFamc();
                            
                            if (fam != null) {
                                System.out.println("--- FAMILLE D'ORIGINE DE " + enfant.getNom() + " ---");
                                
                                // 3. On affiche le Père (en remplaçant l'ID par le Nom)
                                Individu pere = fam.getPere();
                                if (pere != null) System.out.println("Père : " + pere.getNom());
                                else System.out.println("Père : Inconnu");
                                
                                // 4. On affiche la Mère
                                Individu mere = fam.getMere();
                                if (mere != null) System.out.println("Mère : " + mere.getNom());
                                else System.out.println("Mère : Inconnu");
                                
                            } else {
                                System.out.println("Cet individu n'a pas de parents connus dans la base.");
                            }
                        } else {
                            System.out.println("Individu introuvable.");
                        }
                        break;
    
                    default:
                        System.out.println("Commande inconnue. Essayez INFO, CHILD ou SIBLINGS ou autres.");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de la console : " + e.getMessage());
        }
        
        System.out.println("Fin");
    }
}