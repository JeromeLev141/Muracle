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

    public CoordPouce round(int value) throws FractionError {
        x.round(value);
        y.round(value);
        return this;
    }

    @Override
    public String toString(){
        return "X : " + getX() + " | Y : " + getY();
    }
}
