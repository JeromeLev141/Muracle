package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.UUID;

public class Panneau {

    private Pouce largeur;
    private Pouce hauteur;
    private double  poids;
    public UUID id;
    private static final double poidsMatiere = 6.3 / 144;
    /**
     * @brief constructeur vide
     */
    public Panneau() {

    }
    /**
     * @brief constructeur de Panneau
     * @param largeur: Pouce
     * @param hauteur: Pouce
     * @param epaisseur: Pouce
     * @param margeEp: Pouce
     * @param margeLargeurReplis: Pouce
     * @param longeurPlis: Pouce
     * @param type: Char
     * @param isCoin: Pouce
     * @param angleReplis: Pouce
     */

    public Panneau(Pouce hauteur,Pouce largeur, Pouce epaisseur,Pouce margeEp, Pouce margeLargeurReplis,Pouce longeurPlis, char type,boolean isCoin,double angleReplis){
        Pouce margeLarReplis = margeLargeurReplis.mul(2);
        epaisseur = epaisseur.add(margeEp);
        this.largeur = largeur;
        this.hauteur = hauteur;
        double largeurPlis = longeurPlis.toDouble()/Math.tan(Math.toRadians(angleReplis));
        if (Math.tan(Math.toRadians(angleReplis)) == 0)
            largeurPlis = 0;
        double airCoinTriangle = longeurPlis.toDouble() * largeurPlis;
        double surfaceInterieurDouble;

        Pouce surfaceEnPouceCarre = largeur.mul(hauteur);
        if(type == 'i'){
            Pouce largeurReplisInt = largeur.sub(margeLarReplis);
            Pouce surfaceReplisInt = largeurReplisInt.mul(epaisseur);
            Pouce surfaceInterieur = surfaceEnPouceCarre.add(surfaceReplisInt.mul(2));
            if(isCoin){
                Pouce aireRectangle = longeurPlis.mul(largeur.add(epaisseur.sub(margeLarReplis)));

                surfaceInterieur.addRef(aireRectangle.mul(2));
                double surfacePlisCoin = epaisseur.toDouble() * epaisseur.toDouble() / 2;
                surfaceInterieurDouble = surfaceInterieur.toDouble() - 2 * airCoinTriangle + surfacePlisCoin;
            }
            else{
                Pouce aireRectangle = longeurPlis.mul(largeur.sub(margeLarReplis));

                surfaceInterieur.addRef(aireRectangle.mul(2));
                surfaceInterieurDouble = surfaceInterieur.toDouble() - 2 * airCoinTriangle;
            }

            this.poids = surfaceInterieurDouble * poidsMatiere;
        } else if (type == 'e') {

            Pouce hauteurReplisExt = hauteur.sub(margeLarReplis);
            Pouce surfaceReplisExt = hauteurReplisExt.mul(epaisseur);
            Pouce surfaceExterieur = surfaceEnPouceCarre.add(surfaceReplisExt.mul(2));
            Pouce aireRectangle = longeurPlis.mul(hauteurReplisExt);
            
            surfaceExterieur.addRef(aireRectangle.mul(2));

            double poidsSupplementaire = 0;
            if(isCoin){
                double rallongeMur = hauteur.toDouble() * epaisseur.toDouble();
                double hypothenuse = epaisseur.toDouble()/Math.cos(Math.toRadians(45));
                double rallongeMurHyppo = hypothenuse * hauteurReplisExt.toDouble();

                poidsSupplementaire = (rallongeMur + rallongeMurHyppo - aireRectangle.toDouble()) * poidsMatiere;
            }
            this.poids = (surfaceExterieur.toDouble() - 2 * airCoinTriangle) * poidsMatiere + poidsSupplementaire;
        }
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
     * @brief getter Poids
     * @return double
     */
    public double getPoids() {
        return poids;
    }
    /**
     * @brief setter de hauteur
     * @param poids: double
     */
    public void setPoids(double poids) {
        this.poids = poids;
    }
    /**
     * @brief Optient si le poids est valid, vrai si inferieur à 250 sinon faux
     * @return boolean
     */
    public boolean isPoidsValid(){
        return (this.poids <= 250);
    }
    /**
     * @brief Soustrait le poids d'un trou d'accesoire, en fonction du type et du poids du panneau avant le retrait
     * @param largeur: Pouce
     * @param hauteur: Pouce
     * @param marge: Pouce
     * @param type: String
     * @param epTrouRetourAir: Pouce
     * @param interieur: boolean
     * @param epaisseur:Pouce
     * @param margeEp:Pouce
     * @param longeurPlis:Pouce
     */
    public void soustrairePoidsAccessoire(Pouce hauteur,Pouce largeur,Pouce marge,String type,Pouce epTrouRetourAir,boolean interieur,Pouce epaisseur,Pouce margeEp, Pouce longeurPlis){
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
                if(interieur){
                    this.poids = poids - (largeur.mul(epaisseur.add(margeEp.add(longeurPlis))).toDouble() * poidsMatiere);
                }
                this.poids = poids - (aireAccessoirePrise.toDouble() * poidsMatiere);
                break;
            case "Retour d'air":
                Pouce aireAccessoireMurRetourAir = hauteur.mul(largeur);
                Pouce aireAccessoirePlafondRetourAir =  epTrouRetourAir.mul(largeur);
                this.poids = poids - ((aireAccessoireMurRetourAir.toDouble() * poidsMatiere)+(aireAccessoirePlafondRetourAir.toDouble()*poidsMatiere));
                break;
            default:
                break;
        }

    }


}
