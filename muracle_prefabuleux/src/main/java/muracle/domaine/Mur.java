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
    private Pouce position;
    
    /**
     * @brief constructeur vide
     */
    public Mur(){
    }
    /**
     * @brief constructeur de Mur
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param epaisseur:Pouce
     * @param margeEp:Pouce
     * @param margeLargeurReplis:Pouce
     * @param longeurPlis:Pouce
     * @param isCoin:Pouce
     * @param angleReplis:Pouce
     * @param coord:Pouce
     */
    public Mur(Pouce largeur, Pouce hauteur,Pouce epaisseur,Pouce margeEp, Pouce margeLargeurReplis,Pouce longeurPlis, Boolean isCoin,double angleReplis, Pouce coord){
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.panneauExt = new Panneau(hauteur,largeur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,'e',isCoin,angleReplis);
        this.panneauInt = new Panneau(hauteur,largeur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,'i',isCoin,angleReplis);
        this.position = coord;
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
     * @brief getter d'estCoinDroit
     * @return boolean
     */
    public boolean isEstCoinDroit() {
        return estCoinDroit;
    }
    /**
     * @brief setter d'estCoinDroit
     * @param estCoinDroit: boolean
     */
    public void setEstCoinDroit(boolean estCoinDroit) {
        this.estCoinDroit = estCoinDroit;
    }
    /**
     * @brief getter de estCoinGauche
     * @return boolean
     */
    public boolean isEstCoinGauche() {
        return estCoinGauche;
    }
    /**
     * @brief setter d'estCoinGauche
     * @param estCoinGauche: boolean
     */
    public void setEstCoinGauche(boolean estCoinGauche) {
        this.estCoinGauche = estCoinGauche;
    }
    /**
     * @brief getter de panneauExt
     * @return Panneau
     */
    public Panneau getPanneauExt() {
        return panneauExt;
    }
    /**
     * @brief getter de panneauInt
     * @return Panneau
     */
    public Panneau getPanneauInt() {
        return panneauInt;
    }
    /**
     * @brief setter de panneauExt
     * @param panneauExt: Panneau
     */
    public void setPanneauExt(Panneau panneauExt) {
        this.panneauExt = panneauExt;
    }
    /**
     * @brief setter de panneauInt
     * @param panneauInt: Panneau
     */
    public void setPanneauInt(Panneau panneauInt) {
        this.panneauInt = panneauInt;
    }
    /**
     * @brief getter de position
     * @return Pouce
     */
    public Pouce getPosition(){return this.position;}
}
