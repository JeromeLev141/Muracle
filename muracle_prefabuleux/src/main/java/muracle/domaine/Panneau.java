package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.UUID;

public class Panneau {

    private Pouce largeur;
    private Pouce hauteur;
    private double  poids;
    public UUID id;

    public Panneau() {

    }

    public Panneau(Pouce largeur, Pouce hauteur, double poids) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.poids = poids;
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

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public boolean isPoidsValid(){
        return (this.poids <= 250);
    }


}
