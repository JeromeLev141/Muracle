package muracle.domaine;

import muracle.utilitaire.Pouce;

import java.util.UUID;

public class MurDTO {

    public Pouce Largeur;
    public Pouce Hauteur;
    public boolean EstCoinDroit;
    public boolean EstCoinGauche;
    public Panneau PanneauExt;
    public Panneau PanneauInt;
    public UUID id;

    public MurDTO(Mur mur){
        Largeur = mur.getLargeur();
        Hauteur = mur.getHauteur();
        EstCoinDroit = mur.GetEstCoinDroit();
        EstCoinGauche = mur.GetEstCoinGauche();
        PanneauExt = mur.getPanneauExt();
        PanneauInt = mur.getPanneauInt();
    }

}
