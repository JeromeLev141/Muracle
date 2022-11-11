package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.ArrayList;
import java.util.UUID;

public class CoteDTO {

    public char Orientation;
    public Pouce Largeur;
    public Pouce Hauteur;
    public ArrayList<Mur> Murs;
    public ArrayList<Pouce> Separateurs;
    public ArrayList<Accessoire> Accessoires;
    public UUID id;

    CoteDTO(Cote cote){
        Orientation = cote.getOrientation();
        Largeur = cote.getLargeur();
        Hauteur = cote.getHauteur();
        Murs = cote.getMurs();
        Separateurs = cote.getSeparateurs();
        Accessoires = cote.getAccessoires();
    }
}
