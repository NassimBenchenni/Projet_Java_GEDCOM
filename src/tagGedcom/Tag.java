package tagGedcom;

import java.io.Serializable;



/*
 * Class abstraite qui prend en attribut un String
 * les tags gérés sont tagNom et TagSexe pour l'instant.
 */
@SuppressWarnings("serial")
public abstract class Tag implements Serializable {
    
    protected String valeur; // Nom ou Sexe
    
    public Tag(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
    
    public void setValeur(String v) {
        this.valeur = v;
    }
    
    public String toString() {
        return valeur;
    }
}