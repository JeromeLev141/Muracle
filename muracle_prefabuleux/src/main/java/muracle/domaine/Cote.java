package muracle.domaine;

import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;

import java.util.ArrayList;

public class Cote implements java.io.Serializable{
    private char orientation;
    private Pouce largeur;
    private Pouce hauteur;
    private ArrayList<Mur> murs;
    private ArrayList<Pouce> separateurs;

    public Cote(){

    }
    public Cote(char orientation, Pouce largeur, Pouce hauteur){
        this.orientation = orientation;
        this.largeur = largeur;
        this.hauteur = hauteur;

        murs = new ArrayList<>();
        Mur murInit = new Mur(largeur, hauteur);
        murInit.setEstCoinDroit(true);
        murs.add(murInit);
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

    public void setHauteur(Pouce hauteur) {
        this.hauteur = hauteur;
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public ArrayList<Pouce> getSeparateurs() {
        return separateurs;
    }
    public void addSeparateur(Pouce position) throws FractionError {
        int index = 0;
        for (Pouce separateur : this.separateurs) {
            if (position.compare(separateur) == 1) {
                index = index + 1;
            }
        }
        this.separateurs.add(index, position);
        Mur newMur;
        if (index + 1 != this.separateurs.size())
            newMur = new Mur(this.separateurs.get(index + 1).sub(this.separateurs.get(index)),this.getHauteur());
        else {
            newMur = new Mur(getLargeur().sub(separateurs.get(index)), getHauteur());
            newMur.setEstCoinDroit(true);
            murs.get(index).setEstCoinDroit(false);
        }
        murs.add(index + 1, newMur);
    }
    public void deleteSeparateur(int index){
        this.separateurs.remove(index);
    }
    public Pouce getSeparateur(int index){
        return this.separateurs.get(index);
    }
    public void setSeparateur(int index, Pouce position){
        this.separateurs.add(index, position);
    }
    private void updateCoins(){
        //TODO
    }
}
