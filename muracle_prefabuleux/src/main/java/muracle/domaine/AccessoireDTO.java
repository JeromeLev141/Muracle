package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

import java.util.UUID;

public class AccessoireDTO {

    public Pouce largeur;
    public Pouce hauteur;
    public CoordPouce position;
    public String type;
    public Boolean isInterieurOnly;
    public Pouce marge;
    public UUID id;

    AccessoireDTO(Accessoire accessoire){
        largeur = accessoire.getLargeur();
        hauteur = accessoire.getHauteur();
        position = accessoire.getPosition();
        type = accessoire.getType();
        if(type.equals("Fenêtre")){
            marge = accessoire.getMarge();
        }
        isInterieurOnly = accessoire.isInterieurOnly();
    }

}
