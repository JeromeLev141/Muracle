package muracle.domaine;

import muracle.utilitaire.Pouce;

public class Mur implements java.io.Serializable{
    private Pouce largeur;
    private Pouce hauteur;
    private boolean estCoinDroit;
    private boolean estCoinGauche;
    private Panneau panneauExt;
    private Panneau panneauInt;

    private static final double poidsMatiere = 6.3;

    public Mur(){
    }
    public Mur(Pouce largeur, Pouce hauteur){

    }
    public Mur(Pouce largeur, Pouce hauteur,Pouce epaisseur, Pouce margeLargeurReplis,Pouce LongeurPlis){
        Pouce margeLarReplis = margeLargeurReplis.mul(2);
        this.largeur = largeur;
        this.hauteur = hauteur;
        Panneau panExt = new Panneau();
        Panneau panInt = new Panneau();
        panInt.setHauteur(hauteur);
        panInt.setLargeur(largeur);
        panExt.setLargeur(largeur);
        panExt.setHauteur(hauteur);

        Pouce surfaceEnPouceCarre = largeur.mul(hauteur);
        Pouce largeurReplisInt = largeur.sub(margeLarReplis);
        Pouce surfaceReplisInt = largeurReplisInt.mul(epaisseur);
        Pouce surfaceInterieur = surfaceEnPouceCarre.add(surfaceReplisInt.mul(2));

        panInt.setPoids((surfaceInterieur.getEntier() + 1)* poidsMatiere);

        Pouce hauteurReplisExt = hauteur.sub(margeLarReplis);
        Pouce surfaceReplisExt = hauteurReplisExt.mul(epaisseur);
        Pouce surfaceExterieur = surfaceEnPouceCarre.add(surfaceReplisExt.mul(2));

        panExt.setPoids((surfaceExterieur.getEntier() + 1)* poidsMatiere);



        this.panneauExt = panExt;
        this.panneauInt = panInt;
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
