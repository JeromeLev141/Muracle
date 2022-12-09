package muracle.domaine;

import muracle.utilitaire.CoordPouce;

import java.util.ArrayList;
import java.util.List;

public class PlanPanneau {
    private List<CoordPouce> polygone;
    private List<CoordPouce[]> lignePlie;
    private List<List<CoordPouce>> polygoneAccessoire;



    public PlanPanneau(){
        this.polygone = new ArrayList<>();
        this.lignePlie = new ArrayList<>();
        this.polygoneAccessoire = new ArrayList<>();
    }

    public void ajoutPointPolygone(CoordPouce coordPouce){
        polygone.add(coordPouce);
    }

    public void ajoutlignePlie(CoordPouce[] coordPouces){
        lignePlie.add(coordPouces);
    }

    public void ajoutAccessoire(List<CoordPouce> accessoire){
        System.out.println(accessoire);
        polygoneAccessoire.add(accessoire);
    }

    public List<CoordPouce> getPolygone(){
        return polygone;
    }

    public List<CoordPouce[]> getLignePlie(){
        return lignePlie;
    }

    public List<List<CoordPouce>> getPolygoneAccessoire(){
        return polygoneAccessoire;
    }

    @Override
    public String toString(){
        return polygone.toString();
    }
}
