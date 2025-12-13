import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        
        // 1. Initialisation
        Genealogie genealogie = new Genealogie();
        LecteurGedcom lecteur = new LecteurGedcom(genealogie);
        
        // 2. Chargement du fichier
        String fichier = "mon_fichier.ged";
        lecteur.lireFichier(fichier);
        
        // On lance la validation des données après la lecture
        genealogie.validerDonnees();
        
        // 3. Boucle d'interaction avec BufferedReader
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        boolean continuer = true;
        
        try {
            while (continuer) {
                System.out.println("\nEntrez une commande (INFO, CHILD, SIBLINGS, EXIT) :");
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
    
                    default:
                        System.out.println("Commande inconnue. Essayez INFO, CHILD ou SIBLINGS.");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de la console : " + e.getMessage());
        }
        
        System.out.println("Fin");
    }
}