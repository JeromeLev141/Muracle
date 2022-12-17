package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.Pouce;


public class Accessoire implements java.io.Serializable{

    private Pouce largeur;
    private Pouce hauteur;
    private CoordPouce position;
    private String type;
    private Boolean isInterieurOnly;
    private Pouce marge;
    private boolean isValid;
    /**
     * @brief constructeur vide, essai de mettre le marge à 0 et lance une exception si cela échoue
     */
    public Accessoire(){
        try {
            this.marge = new Pouce(0, 0, 1);
        } catch (FractionError e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @brief constructeur d'Accessoire
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param position:CoordPouce
     */
    public Accessoire(Pouce largeur,Pouce hauteur,CoordPouce position){

        this.largeur = largeur;
        this.hauteur = hauteur;
        this.position = position;
        try {
            this.marge = new Pouce(0, 0, 1);
        } catch (FractionError e) {
            throw new RuntimeException(e);
        }
        this.isValid = true;
    }
    /**
     * @brief constructeur d'Accessoire par un accessoire déjà fait
     * @param accessoire:Accessoire
     */
    public Accessoire(Accessoire accessoire){
        this.largeur = accessoire.largeur;
        this.hauteur = accessoire.hauteur;
        this.position = accessoire.position;
        this.marge = accessoire.marge;
        this.type = accessoire.type;
        this.isInterieurOnly = accessoire.isInterieurOnly;
    }
    /**
     * @brief getter de largeur
     * @return Pouce
     */
    public Pouce getLargeur() {
        return largeur;
    }
    /**
     * @brief setter de largeur
     * @param largeur: Pouce
     */
    public void setLargeur(Pouce largeur) {
        this.largeur = largeur;
    }
    /**
     * @brief getter de hauteur
     * @return Pouce
     */
    public Pouce getHauteur() {
        return hauteur;
    }
    /**
     * @brief setter de hauteur
     * @param hauteur: Pouce
     */
    public void setHauteur(Pouce hauteur) {
        this.hauteur = hauteur;
    }
    /**
     * @brief getter de la position (Coin Haut Gauche)
     * @return CoordPouce
     */
    public CoordPouce getPosition() {
        return position;
    }
    /**
     * @brief setter de position
     * @param position: CoordPouce
     */
    public void setPosition(CoordPouce position) {
        this.position = position;
    }
    /**
     * @brief getter de type
     * @return String
     */
    public String getType() {
        return type;
    }
    /**
     * @brief setter de type
     * @param type: String
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @brief getter de la validité de l'accesoire
     * @return boolean
     */
    public boolean isValid(){ return isValid; }
    public void setIsValid(Boolean bool) {this.isValid = bool;}
    /**
     * @brief getter de IntereieurOnly pour
     * savoir si un accessoire est seulement
     * positionné à l'interieur de la sale
     * @return boolean
     */
    public Boolean isInterieurOnly() {
        return isInterieurOnly;
    }
    /**
     * @brief setter de isIntereieurOnly
     * @param interieurOnly:boolean
     */
    public void setInterieurOnly(Boolean interieurOnly) {
        isInterieurOnly = interieurOnly;
    }
    /**
     * @brief setter de Marge
     * @param marge:Pouce
     */
    public void setMarge(Pouce marge) {
        this.marge = marge;
    }
    /**
     * @brief getter de Marge
     * @return Pouce
     */
    public Pouce getMarge() {
        return marge;
    }
}
