package muracle.domaine;

import muracle.utilitaire.*;

import java.util.ArrayList;
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

    public void setAccessoires(ArrayList<Accessoire> accessoires){
        this.accessoires = accessoires;
    }

    public void addAccessoire(Accessoire accessoire) throws FractionError, PouceError, CoteError {
        boolean fitInCote = doesAccessoireFitInCote(accessoire);
        boolean fitWithAccessories = doesAccessoireFitWithOtherAccessoires(accessoire);
        if(fitInCote && fitWithAccessories){
            accessoire.setIsValid(true);
            accessoires.add(accessoire);
        } else if (!fitInCote) {
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        } else {
            accessoire.setIsValid(false);
            accessoires.add(accessoire);
        }
    }
    public boolean doesAccessoireFitInCote(Accessoire accessoire) throws FractionError, PouceError {
        CoordPouce cote1 = new CoordPouce(new Pouce("0"), new Pouce("0"));
        CoordPouce cote2 = new CoordPouce(largeur, hauteur);
        CoordPouce accessoire1;
        CoordPouce accessoire2;
        if(Objects.equals(accessoire.getType(), "Fenêtre")){
            //FIX ICI BAS point peut être 0 si la marge est zero
            /*if(accessoire.getPosition().getX().compare(new Pouce("0")) == 0 || accessoire.getPosition().getY().compare(new Pouce("0")) == 0){
                return false;
            }*/
            accessoire1 = new CoordPouce(accessoire.getPosition().getX().sub(accessoire.getMarge()), accessoire.getPosition().getY().sub(accessoire.getMarge()));
            accessoire2 =new CoordPouce((accessoire.getPosition().getX().add(accessoire.getLargeur()).add(accessoire.getMarge())), (accessoire.getPosition().getY().add(accessoire.getHauteur())).add(accessoire.getMarge()));
        }else{
            accessoire1 = accessoire.getPosition();
            accessoire2 =new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));
        }

        boolean isLeftValid = (cote1.getX().compare(accessoire1.getX()) == -1) || (cote1.getX().compare(accessoire1.getX()) == 0);
        boolean isRightValid = (cote2.getX().compare(accessoire2.getX()) == 1) ||  (cote2.getX().compare(accessoire2.getX()) == 0);
        boolean isTopValid = (cote1.getY().compare(accessoire1.getY()) == -1) || (cote1.getY().compare(accessoire1.getY()) == 0);
        boolean isBotValid = (cote2.getY().compare(accessoire2.getY()) == 1) || (cote2.getY().compare(accessoire2.getY()) == 0);

        return isRightValid && isLeftValid && isBotValid && isTopValid;
    }

    // Suit cet algorithme
    // https://silentmatt.com/rectangle-intersection/
    public boolean doesAccessoireFitWithOtherAccessoires(Accessoire accessoire) {
        CoordPouce mainAccessoire1;
        CoordPouce mainAccessoire2;
        if(Objects.equals(accessoire.getType(), "Fenêtre")){
            mainAccessoire1 = new CoordPouce(accessoire.getPosition().getX().sub(accessoire.getMarge()),
                                             accessoire.getPosition().getY().sub(accessoire.getMarge()));

            mainAccessoire2 = new CoordPouce((accessoire.getPosition().getX().add(accessoire.getLargeur()).add(accessoire.getMarge())),
                                             (accessoire.getPosition().getY().add(accessoire.getHauteur())).add(accessoire.getMarge()));
        }else{
            mainAccessoire1 = accessoire.getPosition();
            mainAccessoire2 = new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));
        }
        if(!accessoires.isEmpty()){
            for(int i = 0; i < accessoires.size(); i++){
                CoordPouce secondAccessoire1;
                CoordPouce secondAccessoire2;
                if(Objects.equals(getAccessoire(i).getType(), "Fenêtre")){
                    secondAccessoire1 =  new CoordPouce(getAccessoire(i).getPosition().getX().sub(getAccessoire(i).getMarge()),
                                                        getAccessoire(i).getPosition().getY().sub(getAccessoire(i).getMarge()));

                    secondAccessoire2 =  new CoordPouce((getAccessoire(i).getPosition().getX().add(getAccessoire(i).getLargeur()).add(getAccessoire(i).getMarge())),
                                                        (getAccessoire(i).getPosition().getY().add(getAccessoire(i).getHauteur())).add(getAccessoire(i).getMarge()));
                }else{
                    secondAccessoire1 = getAccessoire(i).getPosition();
                    secondAccessoire2 = new CoordPouce(getAccessoire(i).getPosition().getX().add(getAccessoire(i).getLargeur()), getAccessoire(i).getPosition().getY().add(getAccessoire(i).getHauteur()));
                }

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
    public void moveAccessoire(Accessoire accessoire, CoordPouce positionPost) throws FractionError, PouceError, CoteError {

        Accessoire dummyAccessoire = new Accessoire(accessoire);
        dummyAccessoire.setPosition(positionPost);
        boolean fitInCote = doesAccessoireFitInCote(dummyAccessoire);
        boolean fitWithAccessories = doesAccessoireFitWithOtherAccessoires(dummyAccessoire);
        if(fitInCote && fitWithAccessories){
            accessoire.setIsValid(true);
            accessoire.setPosition(positionPost);
        } else if (!fitInCote) {
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        } else {
            accessoire.setIsValid(false);
            accessoire.setPosition(positionPost);
        }
    }
    public void removeAccessoire(Accessoire accessoire){
        accessoires.remove(accessoire);
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
    public void addSeparateur(Pouce position) throws CoteError {
        if(position.compare(largeur) == -1 || !separateurs.contains(position)){
            separateurs.add(position);
            sortSeparateur();
        }else{
            throw new CoteError("Position en dehors du côté");
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
    public void setSeparateur(int index, Pouce position) throws CoteError {
        if(!separateurs.contains(position) || (position.compare(largeur) == -1)){
            separateurs.remove(index);
            separateurs.add(position);
            sortSeparateur();
        }else{
            throw new CoteError("Le separateur ajouté chevauche un separateur. Il ne sera pas rajouté");
        }

    }
    public CoordPouce getDimension(){return new CoordPouce(this.largeur,this.hauteur);}

}
