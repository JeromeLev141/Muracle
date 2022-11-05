package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;


public class Accessoire implements java.io.Serializable{

    private Pouce largeur;
    private Pouce hauteur;



    private CoordPouce position;
    private String type;
    private Boolean isInterieurOnly;


    public Accessoire(){

    }
    public Accessoire(Pouce largeur,Pouce hauteur,CoordPouce position){

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

    public Boolean getInterieurOnly() {
        return isInterieurOnly;
    }

    public void setInterieurOnly(Boolean interieurOnly) {
        isInterieurOnly = interieurOnly;
    }
}
