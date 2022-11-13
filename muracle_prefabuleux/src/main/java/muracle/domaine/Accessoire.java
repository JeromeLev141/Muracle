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

        this.largeur = largeur;
        this.hauteur = hauteur;
        this.position = position;

    }
    public Accessoire(Accessoire accessoire){
        this.largeur = accessoire.largeur;
        this.hauteur = accessoire.hauteur;
        this.position = accessoire.position;
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

    public Boolean isInterieurOnly() {
        return isInterieurOnly;
    }

    public void setInterieurOnly(Boolean interieurOnly) {
        isInterieurOnly = interieurOnly;
    }
}
