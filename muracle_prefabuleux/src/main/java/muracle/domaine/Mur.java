package muracle.domaine;

import muracle.utilitaire.Pouce;

public class Mur implements java.io.Serializable{
    private Pouce largeur;
    private Pouce hauteur;
    private boolean estCoinDroit;
    private boolean estCoinGauche;
    private Panneau panneauExt;
    private Panneau panneauInt;



    public Mur(){
    }
    public Mur(Pouce largeur, Pouce hauteur){

    }
    public Mur(Pouce largeur, Pouce hauteur,Pouce epaisseur, Pouce margeLargeurReplis,Pouce longeurPlis, Boolean isCoin,double angleReplis){
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.panneauExt = new Panneau(hauteur,largeur,epaisseur,margeLargeurReplis,longeurPlis,'e',isCoin,angleReplis);
        this.panneauInt = new Panneau(hauteur,largeur,epaisseur,margeLargeurReplis,longeurPlis,'i',isCoin,angleReplis);
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

    public boolean GetEstCoinDroit() {
        return estCoinDroit;
    }

    public void setEstCoinDroit(boolean estCoinDroit) {
        this.estCoinDroit = estCoinDroit;
    }

    public boolean GetEstCoinGauche() {
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
}
