package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class Mur implements java.io.Serializable{
    private Pouce largeur;
    private Pouce hauteur;
    private boolean estCoinDroit;
    private boolean estCoinGauche;
    private Panneau panneauExt;
    private Panneau panneauInt;
    private Accessoire[] accessoires;

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
    public Accessoire getAccessoire(int index){
        return this.accessoires[index];
    }

    public Accessoire[] getAccessoires() {
        return accessoires;
    }
    public void addAccessoire(Accessoire accessoire, CoordPouce positionInit){
        //TODO
    }
    public void moveAccessoire(CoordPouce positionInit, CoordPouce positionPost){
        //TODO
    }
    public void removeAccessoire(CoordPouce position){
        //TODO
    }
    private boolean ContainRetourAir(){
        //TODO
        return true;
    }
    private void updatePoids(){
        //TODO
    }
}
