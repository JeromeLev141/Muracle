package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.UUID;

public class Panneau {

    private Pouce largeur;
    private Pouce hauteur;
    private double  poids;
    public UUID id;
    private static final double poidsMatiere = 6.3;
    public Panneau() {

    }

    public Panneau(Pouce largeur, Pouce hauteur, double poids) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.poids = poids;
    }

    public Panneau(Pouce hauteur,Pouce largeur, Pouce epaisseur, Pouce margeLargeurReplis,Pouce longeurPlis, char type,boolean isCoin){
        Pouce margeLarReplis = margeLargeurReplis.mul(2);
        this.largeur = largeur;
        this.hauteur = hauteur;
        Double largeurPlis = Math.tan(45)*longeurPlis.toDouble();
        Pouce airCoinTriangle = longeurPlis.mul(largeurPlis.intValue() + 1);

        Pouce surfaceEnPouceCarre = largeur.mul(hauteur);
        if(type == 'i'){
            Pouce largeurReplisInt = largeur.sub(margeLarReplis);
            Pouce surfaceReplisInt = largeurReplisInt.mul(epaisseur);
            Pouce surfaceInterieur = surfaceEnPouceCarre.add(surfaceReplisInt.mul(2));

            Pouce longeurPlisAddMargeReplis = longeurPlis.add(margeLargeurReplis);
            Pouce aireRectangle = longeurPlis.mul(largeur.sub(longeurPlisAddMargeReplis.mul(2)));

            surfaceInterieur.add(aireRectangle.mul(2));
            surfaceInterieur.add(airCoinTriangle);

            this.poids = (surfaceInterieur.getEntier() + 1) * poidsMatiere;
        } else if (type == 'e') {
            Pouce hauteurReplisExt = hauteur.sub(margeLarReplis);
            Pouce surfaceReplisExt = hauteurReplisExt.mul(epaisseur);
            Pouce surfaceExterieur = surfaceEnPouceCarre.add(surfaceReplisExt.mul(2));
            Pouce longeurPlisAddMargeReplis = longeurPlis.add(margeLargeurReplis);
            Pouce aireRectangle = longeurPlis.mul(hauteur.sub(longeurPlisAddMargeReplis.mul(2)));

            surfaceExterieur.add(aireRectangle.mul(2));
            surfaceExterieur.add(airCoinTriangle);
            this.poids = (surfaceExterieur.getEntier() + 1)* poidsMatiere;
        }
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

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public boolean isPoidsValid(){
        return (this.poids <= 250);
    }

    public void soustrairePoidsAccessoire(Pouce hauteur,Pouce largeur,Pouce marge,String type,Pouce hauteurRetourAir){
        double poids = this.poids;

        switch (type){
            case "Fenêtre":
                Pouce deuxFoisMarge = marge.mul(2);
                Pouce hauteurAccessoireAvecMarge = hauteur.add(deuxFoisMarge);
                Pouce largeurAccessoireAvecMarge = largeur.add(deuxFoisMarge);
                Pouce aireAccessoireFenetre = hauteurAccessoireAvecMarge.mul(largeurAccessoireAvecMarge);
                this.poids = poids - (aireAccessoireFenetre.toDouble() * poidsMatiere);
                break;
            case "Prise électrique":
            case "Porte":
                Pouce aireAccessoirePrise = hauteur.mul(largeur);
                this.poids = poids - (aireAccessoirePrise.toDouble() * poidsMatiere);
                break;
            case "Retour d'air":
                Pouce aireAccessoireMurRetourAir = hauteur.mul(largeur);
                Pouce aireAccessoirePlafondRetourAir =  hauteurRetourAir.mul(largeur);
                this.poids = poids - ((aireAccessoireMurRetourAir.toDouble() * poidsMatiere)+(aireAccessoirePlafondRetourAir.toDouble()*poidsMatiere));
                break;
            default:
                break;
        }

    }


}
