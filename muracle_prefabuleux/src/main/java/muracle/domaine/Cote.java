package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;
import muracle.utilitaire.PouceError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Cote implements java.io.Serializable{
    private char orientation;
    private Pouce largeur;
    private Pouce hauteur;
    private ArrayList<Mur> murs;
    private ArrayList<Pouce> separateurs;
    private ArrayList<Accessoire> accessoires;

    public Cote(){

    }
    public Cote(char orientation, Pouce largeur, Pouce hauteur){
        this.orientation = orientation;
        this.largeur = largeur;
        this.hauteur = hauteur;
        murs = new ArrayList<>();
        separateurs = new ArrayList<>();
        accessoires = new ArrayList<>();
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
        return accessoires.get(index);
    }

    public ArrayList<Accessoire> getAccessoires() {
        return accessoires;
    }

    //TO REMOVE
    public void setAccessoires(ArrayList<Accessoire> accessoires){
        this.accessoires = accessoires;
    }

    public void addAccessoire(Accessoire accessoire) throws FractionError, PouceError {
        if(doesAccessoireFitInCote(accessoire) && doesAccessoireFitWithOtherAccessoires(accessoire)){
            accessoires.add(accessoire);
        }
    }
    public boolean doesAccessoireFitInCote(Accessoire accessoire) throws FractionError, PouceError {
        CoordPouce cote1 = new CoordPouce(new Pouce("0"), new Pouce("0"));
        CoordPouce cote2 = new CoordPouce(largeur, hauteur);
        CoordPouce accessoire1 = accessoire.getPosition();
        CoordPouce accessoire2 =new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));

        boolean isLeftValid = (cote1.getX().compare(accessoire1.getX()) == -1) || (cote1.getX().compare(accessoire1.getX()) == 0);
        boolean isRightValid = (cote2.getX().compare(accessoire2.getX()) == 1) ||  (cote2.getX().compare(accessoire2.getX()) == 0);
        boolean isTopValid = (cote1.getY().compare(accessoire1.getY()) == -1) || (cote1.getY().compare(accessoire1.getY()) == 0);
        boolean isBotValid = (cote2.getY().compare(accessoire2.getY()) == 1) || (cote2.getY().compare(accessoire2.getY()) == 0);

        return isRightValid && isLeftValid && isBotValid && isTopValid;
    }

    // Suit cet algorithme
    // https://silentmatt.com/rectangle-intersection/
    public boolean doesAccessoireFitWithOtherAccessoires(Accessoire accessoire) throws FractionError {
        CoordPouce mainAccessoire1 = accessoire.getPosition();
        CoordPouce mainAccessoire2 = new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));
        if(!accessoires.isEmpty()){
            for(int i = 0; i < accessoires.size(); i++){
                CoordPouce secondAccessoire1 = getAccessoire(i).getPosition();
                CoordPouce secondAccessoire2 = new CoordPouce(getAccessoire(i).getPosition().getX().add(getAccessoire(i).getLargeur()), getAccessoire(i).getPosition().getY().add(getAccessoire(i).getHauteur()));

                if((mainAccessoire1.getX().compare(secondAccessoire2.getX()) == -1) &&
                        mainAccessoire2.getX().compare(secondAccessoire1.getX()) == 1 &&
                        mainAccessoire1.getY().compare(secondAccessoire2.getY()) == -1 &&
                        mainAccessoire2.getY().compare(secondAccessoire1.getY()) == 1 ){
                    return false;
                }
            }
        }
        return true;
    }
    public void moveAccessoire(CoordPouce positionInit, CoordPouce positionPost){
        //check if position valide
    }
    public void removeAccessoire(CoordPouce position){
        //TODO
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
    public CoordPouce getDimension(){return new CoordPouce(this.largeur,this.hauteur);}

}
