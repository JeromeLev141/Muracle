package muracle.utilitaire;

public class Fraction implements java.io.Serializable{

    private long num;
    private long denum;

    //Constructeur de la class
    public Fraction(int numerateur, int denonimateur) throws FractionError {
        if (denonimateur == 0)
            throw new FractionError("Fraction sur 0");
        num = numerateur;
        denum = denonimateur;
        checkNeg();
    }

    public Fraction(long numerateur, long denonimateur) throws FractionError {
        if (denonimateur == 0)
            throw new FractionError("Fraction sur 0");
        num = numerateur;
        denum = denonimateur;
        checkNeg();
    }

    //Génère une copy de la Fraction
    public Fraction copy(){
        try {
            return new Fraction(this.num, this.denum);
        }catch (FractionError ignored){
            return null;
        }
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
        checkNeg();
    }

    public long getDenum() {
        return denum;
    }

    public void setDenum(long denum) {
        this.denum = denum;
        checkNeg();
    }

    //fait une addition par reference
    public Fraction addRef(Fraction fraction){
        long numfraction = fraction.num * this.denum;
        this.num *= fraction.denum;
        this.num += numfraction;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    //fait une addition par reference copy
    public Fraction add(Fraction fraction){
        return this.copy().addRef(fraction);
    }

    public Fraction addRef(int val){
        val *= this.denum;
        this.num += val;
        simplifier();
        return this;
    }

    //fait une addition par reference copy
    public Fraction add(int val){
        return this.copy().addRef(val);
    }

    public Fraction subRef(int val){
        val *= this.denum;
        this.num -= val;
        simplifier();
        return this;
    }

    //fait une addition par reference copy
    public Fraction sub(int val){
        return this.copy().subRef(val);
    }

    //fait une soustraction par reference
    public Fraction subRef(Fraction fraction){
        long numfraction = fraction.num * this.denum;
        this.num *= fraction.denum;
        this.num -= numfraction;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    //fait une soustraction par reference copy
    public Fraction sub(Fraction fraction){
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
    public Fraction mul(Fraction fraction){
        return this.copy().mulRef(fraction);
    }

    public Fraction mulRef(int value){
        this.num *= value;
        return this;
    }

    public Fraction mul(int value){
        return this.copy().mulRef(value);
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

    public Fraction divRef(int diviseur) throws FractionError{
        if (diviseur == 0){
            throw new FractionError("Division par zero");
        }
        this.denum *= diviseur;
        simplifier();
        return this;
    }

    public Fraction div(int diviseur) throws FractionError{
        return this.copy().divRef(diviseur);
    }


    /**
     @brief arroudie la fraction au dénominateur donnée.
     **/
    public Fraction round(int value) throws FractionError {
        if (value <= 0)
            throw new FractionError("Valeur ne peut pas etre <= 0");
        this.num = (int)Math.round((double)this.num * value / this.denum);
        this.denum = value;
        simplifier();
        return this;
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
    private long pgcd(long num, long denum){
        if (denum == 0)
            return num;
        return pgcd(denum, num%denum);
    }

    public void simplifier(){
        long pgcd = pgcd(num,denum);
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
