package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class RetourAir extends Accessoire {
    /**
     * @brief constructeur de retourAir
     */
    public RetourAir(){
        this.setType("Retour d'air");
        this.setInterieurOnly(Boolean.TRUE);
    }
    /**
     * @brief constructeur de RetourAir, avec comme type Retour d'air et l'attribue interieur seulement à true
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param position:CoordPouce
     */
    public RetourAir(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("Retour d'air");
        this.setInterieurOnly(Boolean.TRUE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }
}
