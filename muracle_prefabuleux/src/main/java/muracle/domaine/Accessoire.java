package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;


public class Accessoire implements java.io.Serializable{

    private Pouce largeur;
    private Pouce hauteur;
    private CoordPouce position;
    private String type;
    private Boolean isInterieurOnly;

    private Pouce marge;

    private boolean isValid;
    public Accessoire(){

    }
    public Accessoire(Pouce largeur,Pouce hauteur,CoordPouce position){

        this.largeur = largeur;
        this.hauteur = hauteur;
        this.position = position;
        this.isValid = true;
    }
    public Accessoire(Accessoire accessoire){
        this.largeur = accessoire.largeur;
        this.hauteur = accessoire.hauteur;
        this.position = accessoire.position;
        this.marge = accessoire.marge;
        this.type = accessoire.type;
        this.isInterieurOnly = accessoire.isInterieurOnly;
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

    public CoordPouce getPosition() {
        return position;
    }

    public void setPosition(CoordPouce position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValid(){ return isValid; }
    public void setIsValid(Boolean bool) {this.isValid = bool;}

    public Boolean isInterieurOnly() {
        return isInterieurOnly;
    }

    public void setInterieurOnly(Boolean interieurOnly) {
        isInterieurOnly = interieurOnly;
    }

    public void setMarge(Pouce marge) {
        this.marge = marge;
    }
    public Pouce getMarge() {
        return marge;
    }
}
