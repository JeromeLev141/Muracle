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
            accessoires.add(accessoire);
        } else if (!fitInCote) {
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        } else {
            throw new CoteError("Accessoire intersecte avec les autres accessoires");
        }
    }
    public boolean doesAccessoireFitInCote(Accessoire accessoire) throws FractionError, PouceError {
        CoordPouce cote1 = new CoordPouce(new Pouce("0"), new Pouce("0"));
        CoordPouce cote2 = new CoordPouce(largeur, hauteur);
        CoordPouce accessoire1;
        CoordPouce accessoire2;
        if(Objects.equals(accessoire.getType(), "Fenetre")){
            Pouce jeuSupplementaire = new Pouce(0, new Fraction(1,8));
            accessoire1 = new CoordPouce(accessoire.getPosition().getX().add(jeuSupplementaire), accessoire.getPosition().getY().add(jeuSupplementaire));
            accessoire2 =new CoordPouce((accessoire.getPosition().getX().add(accessoire.getLargeur()).add(jeuSupplementaire)), (accessoire.getPosition().getY().add(accessoire.getHauteur())).add(jeuSupplementaire));
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
    public boolean doesAccessoireFitWithOtherAccessoires(Accessoire accessoire) throws FractionError {
        CoordPouce mainAccessoire1;
        CoordPouce mainAccessoire2;
        if(Objects.equals(accessoire.getType(), "Fenetre")){
            Pouce jeuSupplementaire = new Pouce(0, new Fraction(1,8));
            mainAccessoire1 = new CoordPouce(accessoire.getPosition().getX().add(jeuSupplementaire), accessoire.getPosition().getY().add(jeuSupplementaire));
            mainAccessoire2 = new CoordPouce((accessoire.getPosition().getX().add(accessoire.getLargeur()).add(jeuSupplementaire)), (accessoire.getPosition().getY().add(accessoire.getHauteur())).add(jeuSupplementaire));
        }else{
            mainAccessoire1 = accessoire.getPosition();
            mainAccessoire2 = new CoordPouce(accessoire.getPosition().getX().add(accessoire.getLargeur()), accessoire.getPosition().getY().add(accessoire.getHauteur()));
        }
        if(!accessoires.isEmpty()){
            for(int i = 0; i < accessoires.size(); i++){
                CoordPouce secondAccessoire1;
                CoordPouce secondAccessoire2;
                if(Objects.equals(getAccessoire(i).getType(), "Fenetre")){
                    Pouce jeuSupplementaire = new Pouce(0, new Fraction(1,8));
                    secondAccessoire1 =  new CoordPouce(getAccessoire(i).getPosition().getX().add(jeuSupplementaire), getAccessoire(i).getPosition().getY().add(jeuSupplementaire));
                    secondAccessoire2 =  new CoordPouce((getAccessoire(i).getPosition().getX().add(getAccessoire(i).getLargeur()).add(jeuSupplementaire)), (getAccessoire(i).getPosition().getY().add(getAccessoire(i).getHauteur())).add(jeuSupplementaire));
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
            accessoire.setPosition(positionPost);
        } else if (!fitInCote) {
            throw new CoteError("Accessoire ne rentre pas dans le côté");
        } else {
            throw new CoteError("Accessoire intersecte avec les autres accessoires");
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
