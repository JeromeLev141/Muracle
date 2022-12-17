package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class Porte extends Accessoire {
    /**
     * @brief constructeur de Porte vide
     */
    public Porte(){
        this.setType("Porte");
        this.setInterieurOnly(Boolean.FALSE);
    }
    /**
     * @brief constructeur de Porte, avec comme type Porte et l'attribue interieur seulement à faux
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param position:CoordPouce
     */
    public Porte(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("Porte");
        this.setInterieurOnly(Boolean.FALSE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }

}
