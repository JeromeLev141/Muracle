package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

import java.util.ArrayList;
import java.util.UUID;

public class CoteDTO {

    public char orientation;
    public Pouce largeur;
    public Pouce hauteur;
    public ArrayList<Mur> murs;
    public ArrayList<Pouce> separateurs;
    public ArrayList<Accessoire> accessoires;
    public UUID id;

    CoteDTO(Cote cote) {
        orientation = cote.getOrientation();
        largeur = cote.getLargeur();
        hauteur = cote.getHauteur();
        murs = null;
        separateurs = cote.getSeparateurs();
        accessoires = cote.getAccessoires();
    }
    CoteDTO(Cote cote, Pouce epaisseur, Pouce margEp, Pouce margeLargeurReplis,Pouce longeurPlis,Pouce epTrouRetourAir, double anglePlis){
        orientation = cote.getOrientation();
        largeur = cote.getLargeur();
        hauteur = cote.getHauteur();
        murs = cote.getMurs(epaisseur, margEp, margeLargeurReplis, longeurPlis, epTrouRetourAir, anglePlis);
        separateurs = cote.getSeparateurs();
        accessoires = cote.getAccessoires();
    }

    public CoordPouce getDimension(){return new CoordPouce(this.largeur,this.hauteur);}
}
