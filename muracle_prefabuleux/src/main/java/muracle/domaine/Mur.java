package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

import java.util.Objects;

public class Mur implements java.io.Serializable{
    private Pouce largeur;
    private Pouce hauteur;
    private boolean estCoinDroit;
    private boolean estCoinGauche;
    private Panneau panneauExt;
    private Panneau panneauInt;

    public Mur(){
    }
    public Mur(Pouce largeur, Pouce hauteur){

    }

    public Pouce getLargeur() {
        return largeur;
    }

    public void setLargeur(Pouce largeur) {
        this.largeur = largeur;
    }

    public Pouce getHauteur() {
        return hauteur;
    }

    public void setHauteur(Pouce hauteur) {
        this.hauteur = hauteur;
    }

    public boolean GetEstCoinDroit() {
        return estCoinDroit;
    }

    public void setEstCoinDroit(boolean estCoinDroit) {
        this.estCoinDroit = estCoinDroit;
    }

    public boolean GetEstCoinGauche() {
        return estCoinGauche;
    }

    public void setEstCoinGauche(boolean estCoinGauche) {
        this.estCoinGauche = estCoinGauche;
    }

    public Panneau getPanneauExt() {
        return panneauExt;
    }
    public Panneau getPanneauInt() {
        return panneauInt;
    }
    private boolean ContainRetourAir(){
        return false;
    }
}
