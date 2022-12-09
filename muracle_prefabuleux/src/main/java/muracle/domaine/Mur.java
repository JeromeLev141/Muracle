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



    public Mur(){
    }
    public Mur(Pouce largeur, Pouce hauteur){

    }
    public Mur(Pouce largeur, Pouce hauteur,Pouce epaisseur,Pouce margeEp, Pouce margeLargeurReplis,Pouce longeurPlis, Boolean isCoin,double angleReplis, Pouce coord){
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.panneauExt = new Panneau(hauteur,largeur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,'e',isCoin,angleReplis);
        this.panneauInt = new Panneau(hauteur,largeur,epaisseur,margeEp,margeLargeurReplis,longeurPlis,'i',isCoin,angleReplis);
        this.position = coord;
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

    public boolean isEstCoinDroit() {
        return estCoinDroit;
    }

    public void setEstCoinDroit(boolean estCoinDroit) {
        this.estCoinDroit = estCoinDroit;
    }

    public boolean isEstCoinGauche() {
        return estCoinGauche;
    }

    public void setEstCoinGauche(boolean estCoinGauche) {
        this.estCoinGauche = estCoinGauche;
    }

    public Panneau getPanneauExt() {
        return panneauExt;
    }
    public Panneau getPanneauInt() {
        return panneauInt;
    }

    public void setPanneauExt(Panneau panneauExt) {
        this.panneauExt = panneauExt;
    }

    public void setPanneauInt(Panneau panneauInt) {
        this.panneauInt = panneauInt;
    }

    public Pouce getPosition(){return this.position;}
}
