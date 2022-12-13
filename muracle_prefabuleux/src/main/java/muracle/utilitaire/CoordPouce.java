package muracle.utilitaire;

public class CoordPouce implements java.io.Serializable{

    private Pouce x;
    private Pouce y;

    /**
     * @brief constructeur de la classe CoordPouce
     * @param x: coordonné en X
     * @param y: coordonné en Y
     */
    public CoordPouce(Pouce x,Pouce y){
        this.x = x;
        this.y = y;
    }

    /**
     * @brief getteur de l'attribut X
     * @return Pouce
     */
    public Pouce getX() {
        return x;
    }

    /**
     * @brief setteur de l'attribut X
     * @param x: Pouce
     */
    public void setX(Pouce x) {
        this.x = x;
    }

    /**
     * @brief getteur de l'attribut Y
     * @return Pouce
     */
    public Pouce getY() {
        return y;
    }

    /**
     * @brief setteur de l'attribut Y
     * @param y:Pouce
     */
    public void setY(Pouce y) {
        this.y = y;
    }

    /**
     * @brief arroudie X et Y à value
     * @param value (int) : arroudie a cette valeur
     * @return CoordPouce
     * @throws FractionError: erreur si la valeur passée en paramètre est invalide
     */
    public CoordPouce round(int value) throws FractionError {
        x.round(value);
        y.round(value);
        return this;
    }

    /**
     * @brief génère une copy en profondeur de l'objet
     * @return CoordPouce
     */
    public CoordPouce copy(){
        return new CoordPouce(this.x.copy(),this.y.copy());
    }

    @Override
    public String toString(){
        return "(" +getX() + ", " + getY() + ")";
    }
}
