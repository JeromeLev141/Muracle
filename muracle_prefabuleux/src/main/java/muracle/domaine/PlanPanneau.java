package muracle.domaine;

import muracle.utilitaire.CoordPouce;
import muracle.utilitaire.Pouce;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlanPanneau {
    private List<CoordPouce> polygone;
    private List<CoordPouce[]> lignePlie;
    private List<List<CoordPouce>> polygoneAccessoire;


    /**
     * @biref constructeur de la class PlanPanneau
     */
    public PlanPanneau(){
        this.polygone = new ArrayList<>();
        this.lignePlie = new ArrayList<>();
        this.polygoneAccessoire = new ArrayList<>();
    }

    /**
     * @brief ajout un points dans la liste polygone
     * @param coordPouce: coordonnée du point
     */
    public void ajoutPointPolygone(CoordPouce coordPouce){
        polygone.add(coordPouce);
    }

    /**
     * @brief ajout un tableau de 2 coordPouce qui représente une ligne de plie
     * @param coordPouces
     */
    public void ajoutlignePlie(CoordPouce[] coordPouces){
        lignePlie.add(coordPouces);
    }

    /**
     * @brief ajout une list de CoordPouce qui représente un accessoir
     * @param accessoire: list<CoordPouce>
     */
    public void ajoutAccessoire(List<CoordPouce> accessoire){
        System.out.println(accessoire);
        polygoneAccessoire.add(accessoire);
    }

    /**
     * @brief getteur de l'attribut polygone
     * @return polygone: List<CoordPouce>
     */
    public List<CoordPouce> getPolygone(){
        return polygone;
    }

    /**
     * @brief getteur de l'attribut lignePlie
     * @return polygone: List<CoordPouce[]>
     */
    public List<CoordPouce[]> getLignePlie(){
        return lignePlie;
    }

    /**
     * @brief getteur de l'attribut polygoneAccessoire
     * @return polygone: List<List<CoordPouce>>
     */
    public List<List<CoordPouce>> getPolygoneAccessoire(){
        return polygoneAccessoire;
    }

    /**
     * @brief retourne la largeur du plan
     * @return Pouce
     */
    public Pouce getLargeur(){
        return polygone.stream().max(Comparator.comparing(i -> i.getX().toDouble())).get().getX();
    }

    /**
     * @brief retourne la hauteur du plan
     * @return Pouce
     */
    public Pouce getHauteur(){
        return polygone.stream().max(Comparator.comparing(i -> i.getY().toDouble())).get().getY();
    }


    @Override
    public String toString(){
        return polygone.toString();
    }
}
