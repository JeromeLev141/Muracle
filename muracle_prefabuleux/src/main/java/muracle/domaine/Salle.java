package muracle.domaine;

import muracle.utilitaire.*;

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
        this.epaisseurTrouRetourAir = new Pouce("6");
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
        return null;
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
        getCote('N').setLargeur(largeur);
        getCote('S').setLargeur(largeur);
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
        getCote('E').setHauteur(hauteur);
        getCote('W').setHauteur(hauteur);
        getCote('N').setHauteur(hauteur);
        getCote('S').setHauteur(hauteur);
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
        getCote('E').setLargeur(longueur);
        getCote('W').setLargeur(longueur);
    }

    public Pouce getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(Pouce profondeur) {
        this.profondeur = profondeur;
    }

    public CoordPouce getDimension() throws FractionError {
        return new CoordPouce(this.largeur.add(this.profondeur.mul(new Fraction(2,1))),
                this.longueur.add(this.profondeur.mul(new Fraction(2,1))));
    }
}
