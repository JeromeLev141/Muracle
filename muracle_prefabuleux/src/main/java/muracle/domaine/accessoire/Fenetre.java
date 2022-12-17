package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class Fenetre extends Accessoire {
    /**
     * @brief constructeur de fenetre vide
     */
    public Fenetre(){
        this.setType("Fenêtre");
        this.setInterieurOnly(Boolean.FALSE);
    }
    /**
     * @brief constructeur de Fenetre, avec comme type Fenêtre et l'attribue interieur seulement à faux
     * @param largeur:Pouce
     * @param hauteur:Pouce
     * @param position:CoordPouce
     */
    public Fenetre(Pouce largeur, Pouce hauteur, CoordPouce position){

        this.setType("Fenêtre");
        this.setInterieurOnly(Boolean.FALSE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }

}
