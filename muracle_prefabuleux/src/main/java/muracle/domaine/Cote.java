package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import java.util.ArrayList;
import java.util.Collections;

public class Cote implements java.io.Serializable{
    private char orientation;
    private Pouce largeur;
    private Pouce hauteur;
    private ArrayList<Mur> murs;
    private ArrayList<Pouce> separateurs;
    private Accessoire[] accessoires;

    public Cote(){

    }
    public Cote(char orientation, Pouce largeur, Pouce hauteur){
        this.orientation = orientation;
        this.largeur = largeur;
        this.hauteur = hauteur;
        murs = new ArrayList<>();
        separateurs = new ArrayList<>();
    }

    public char getOrientation() {
        return orientation;
    }

    public void setOrientation(char orientation) {
        this.orientation = orientation;
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
    public Accessoire getAccessoire(int index){
        return this.accessoires[index];
    }

    public Accessoire[] getAccessoires() {
        return accessoires;
    }

    public void addAccessoire(Accessoire accessoire, CoordPouce positionInit){

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

    public void setHauteur(Pouce hauteur) {
        this.hauteur = hauteur;
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public ArrayList<Pouce> getSeparateurs() {
        return separateurs;
    }
    public void addSeparateur(Pouce position) throws PouceError {
        if(position.compare(largeur) == -1){
            separateurs.add(position);
            sortSeparateur();
        }else{
            throw new PouceError("Position en dehors du côté");
        }
    }
    private void sortSeparateur(){
        separateurs.sort(Pouce::compare);
    }
    public void deleteSeparateur(int index){
        this.separateurs.remove(index);
    }
    public Pouce getSeparateur(int index){
        return this.separateurs.get(index);
    }
    public void setSeparateur(int index, Pouce position){
        separateurs.get(index).setEntier(position.getEntier());
        separateurs.get(index).setFraction(position.getFraction());
        sortSeparateur();
    }
}
