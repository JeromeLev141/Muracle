package muracle.domaine.accessoire;

import muracle.domaine.Accessoire;
import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

public class PriseElec extends Accessoire {

    public PriseElec(){
        this.setType("PriseElec");
        this.setInterieurOnly(Boolean.TRUE);
    }

    public PriseElec(Pouce largeur, Pouce hauteur, CoordPouce position){
        this.setType("PriseElec");
        this.setInterieurOnly(Boolean.TRUE);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setPosition(position);
    }
}
