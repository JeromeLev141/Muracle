package muracle.utilitaire;

public class CoordPouce implements java.io.Serializable{

    private Pouce x;
    private Pouce y;

    public CoordPouce(Pouce x,Pouce y){
        this.x = x;
        this.y = y;
    }

    public Pouce getX() {
        return x;
    }

    public void setX(Pouce x) {
        this.x = x;
    }

    public Pouce getY() {
        return y;
    }

    public void setY(Pouce y) {
        this.y = y;
    }


    //à implémenter dans pouce calcule de la racide carré
    //Fonction incomplet
    public static Pouce distance(CoordPouce pts1, CoordPouce pts2) throws FractionError {
        Pouce x = pts1.getX().sub(pts2.getX());
        Pouce y = pts1.getY().sub(pts2.getY());
        return null;
    }

    @Override
    public String toString(){
        return "X : " + getX() + " | Y : " + getY();
    }
}
