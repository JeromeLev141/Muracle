package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class PriseElec extends Accessoire {
    /**
     * @brief constructeur de PriseElec vide
     */
    public PriseElec(){
        this.setType("Prise électrique");
        this.setInterieurOnly(Boolean.TRUE);
    }
    /**
     * @brief constructeur de PriseElec, avec comme type Prise électrique et l'attribue interieur seulement à true
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param position:CoordPouce
     */
    public PriseElec(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("Prise électrique");
        this.setInterieurOnly(Boolean.TRUE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }
}
