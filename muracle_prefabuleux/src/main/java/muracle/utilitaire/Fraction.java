package muracle.utilitaire;
import java.lang.Math.*;

public class Fraction {

    private int num;
    private int denum;

    //Constructeur de la class
    public Fraction(int numerateur, int denonimateur) throws FractionError {
        if (denonimateur == 0)
            throw new FractionError("Fraction sur 0");
        num = numerateur;
        denum = denonimateur;
        checkNeg();
    }

    //Génère une copy de la Fraction
    public Fraction copy() throws FractionError {
        return new Fraction(this.num,this.denum);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        checkNeg();
    }

    public int getDenum() {
        return denum;
    }

    public void setDenum(int denum) {
        this.denum = denum;
        checkNeg();
    }

    //fait une addition par reference
    public Fraction addRef(Fraction fraction){
        int numfraction = fraction.num * this.denum;
        this.num *= fraction.denum;
        this.num += numfraction;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    //fait une addition par reference copy
    public Fraction add(Fraction fraction) throws FractionError {
        return this.copy().addRef(fraction);
    }

    //fait une soustraction par reference
    public Fraction subRef(Fraction fraction){
        int numfraction = fraction.num * this.denum;
        this.num *= fraction.denum;
        this.num -= numfraction;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    //fait une soustraction par reference copy
    public Fraction sub(Fraction fraction) throws FractionError {
        return this.copy().subRef(fraction);
    }

    //fait une multiplication par reference
    public Fraction mulRef(Fraction fraction){
        this.num *= fraction.num;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    //fait une multiplication par reference copy
    public Fraction mul(Fraction fraction) throws FractionError {
        return this.copy().mulRef(fraction);
    }

    public Fraction mulRef(int value){
        this.num *= value;
        return this;
    }

    //fait une division par reference
    public Fraction divRef(Fraction fraction) throws FractionError {
        if (fraction.num == 0)
            throw  new FractionError("Division par zero");
        this.num *= fraction.denum;
        this.denum *= fraction.num;
        simplifier();
        return this;
    }

    //fait une division par reference copy
    public Fraction div(Fraction fraction) throws FractionError {
        return this.copy().divRef(fraction);
    }

    // equivalent a ==
    public boolean equals(Fraction fraction){
        return this.toDouble() == fraction.toDouble();
    }

    //compare deux Fractions -1 <, 0 ==, 1 >
    public int compare(Fraction fraction){
        return Double.compare(toDouble(), fraction.toDouble());
    }

    //retourne la valuer double de la fraction
    public double toDouble(){
        return (double) this.num / this.denum;
    }

    //calcule le pgcd de la fraction
    //source: https://stackoverflow.com/questions/4009198/java-get-greatest-common-divisor
    private int pgcd(int num, int denum){
        if (denum == 0)
            return num;
        return pgcd(denum, num%denum);
    }

    public void simplifier(){
        int pgcd = pgcd(num,denum);
        num /= pgcd;
        denum /= pgcd;
        checkNeg();
    }

    private void checkNeg(){
        if (denum < 0){
            num *= -1;
            denum *= -1;
        }
    }

    @Override
    public String toString() {
        return num + "/" + denum;
    }
}
