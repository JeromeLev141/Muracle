package muracle.domaine;

import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

public class Salle implements java.io.Serializable{
    private Cote[] tableauCote;
    private Pouce largeur;
    private Pouce longueur;
    private Pouce hauteur;
    private Pouce profondeur;
    private Pouce hauteurRetourAir;
    private Pouce epaisseurTrouRetourAir;
    private Pouce distanceTrouRetourAir;

    public Salle(){
    }
    public Salle(Pouce largeur,Pouce hauteur, Pouce longueur, Pouce profondeur) throws FractionError, PouceError {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.longueur = longueur;
        this.profondeur = profondeur;
        this.distanceTrouRetourAir = new Pouce("10");
        this.epaisseurTrouRetourAir = new Pouce("8");
        this.hauteurRetourAir = new Pouce("12");

        tableauCote = new Cote[]{new Cote('S', largeur, hauteur), new Cote('E', longueur, hauteur),
        new Cote('N', largeur, hauteur), new Cote('W', longueur, hauteur)};
    }

    public Cote[] getTableauCote() {
        return tableauCote;
    }
    public Cote getCote(char orientation){
        for (Cote cote : this.tableauCote) {
            if (cote.getOrientation() == orientation) {
                return cote;
            }
        }
        return new Cote();
    }

    public Pouce getDistanceTrouRetourAir() {
        return distanceTrouRetourAir;
    }

    public void setDistanceTrouRetourAir(Pouce distanceTrouRetourAir) {
        this.distanceTrouRetourAir = distanceTrouRetourAir;
    }

    public Pouce getLargeur(){
        return largeur;
    }

    public void setLargeur(Pouce largeur) {
        this.largeur = largeur;
    }

    public Pouce getEpaisseurTrouRetourAir() {
        return epaisseurTrouRetourAir;
    }

    public void setEpaisseurTrouRetourAir(Pouce epaisseurTrouRetourAir) {
        this.epaisseurTrouRetourAir = epaisseurTrouRetourAir;
    }

    public Pouce getHauteur() {
        return hauteur;
    }

    public void setHauteur(Pouce hauteur) {
        this.hauteur = hauteur;
    }

    public Pouce getHauteurRetourAir() {
        return hauteurRetourAir;
    }

    public void setHauteurRetourAir(Pouce hauteurRetourAir) {
        this.hauteurRetourAir = hauteurRetourAir;
    }

    public Pouce getLongueur() {
        return longueur;
    }

    public void setLongueur(Pouce longueur) {
        this.longueur = longueur;
    }

    public Pouce getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(Pouce profondeur) {
        this.profondeur = profondeur;
    }
}
