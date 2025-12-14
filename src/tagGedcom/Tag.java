package tagGedcom;

import java.io.Serializable;


@SuppressWarnings("serial")
public abstract class Tag implements Serializable {
    
    protected String valeur; // Exemple: "John /Begood/" ou "M"
    
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