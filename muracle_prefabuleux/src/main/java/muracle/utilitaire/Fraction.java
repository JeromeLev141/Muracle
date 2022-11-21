package muracle.utilitaire;

public class Fraction implements java.io.Serializable{

    private long num;
    private long denum;

    /**
     * @brief constructeur de la classe Fractino
     * @param numerateur: int
     * @param denonimateur: int
     * @throws FractionError si denum = 0
     */
    public Fraction(int numerateur, int denonimateur) throws FractionError {
        if (denonimateur == 0)
            throw new FractionError("Fraction sur 0");
        num = numerateur;
        denum = denonimateur;
        checkNeg();
    }

    /**
     * @brief constructeur prive de la fonction
     * @param numerateur:long
     * @param denonimateur:long
     * @throws FractionError si denum = 0
     */
    private Fraction(long numerateur, long denonimateur) throws FractionError {

        if (denonimateur == 0)
            throw new FractionError("Fraction sur 0");
        num = numerateur;
        denum = denonimateur;
        checkNeg();
    }

    /**
     * @brief fait une copy en profondeur de l'objet
     * @return Fraction
     */
    public Fraction copy(){
        try {
            return new Fraction(this.num, this.denum);
        }catch (FractionError ignored){
            return null;
        }
    }

    /**
     * @brief getteur du numerateur
     * @return this.num
     */
    public long getNum() {
        return num;
    }

    /**
     * @brief setteur du numerateur
     * @param num:long
     */
    public void setNum(long num) {
        this.num = num;
        checkNeg();
    }

    /**
     * @brief getteur du denominateur
     * @return this.denum
     */
    public long getDenum() {
        return denum;
    }

    /**
     * @brief setteur du denominateur
     * @param denum:long
     */
    public void setDenum(long denum) {
        this.denum = denum;
        checkNeg();
    }

    /**
     * @brief addition modifiant l'objet
     * @param fraction:Fraction
     * @return Fraction
     */
    public Fraction addRef(Fraction fraction){
        long numfraction = fraction.num * this.denum;
        this.num *= fraction.denum;
        this.num += numfraction;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    /**
     * @brief addition créant un nouvel objet
     * @param fraction:Fraction
     * @return Fraction
     */
    public Fraction add(Fraction fraction){
        return this.copy().addRef(fraction);
    }

    /**
     * @brief addition modifiant l'objet
     * @param value:Int
     * @return Fraction
     */
    public Fraction addRef(int value){
        value *= this.denum;
        this.num += value;
        simplifier();
        return this;
    }

    /**
     * @brief addition créant un nouvel objet
     * @param value:Int
     * @return Fraction
     */
    public Fraction add(int value){
        return this.copy().addRef(value);
    }

    /**
     * @brief soustraction modifiant l'objet
     * @param value:Int
     * @return Fraction
     */
    public Fraction subRef(int value){
        value *= this.denum;
        this.num -= value;
        simplifier();
        return this;
    }

    /**
     * @brief soustraction créant un nouvel objet
     * @param value:Int
     * @return Fraction
     */
    public Fraction sub(int value){
        return this.copy().subRef(value);
    }

    /**
     * @brief soustraction modifiant l'objet
     * @param fraction:Fraction
     * @return Fraction
     */
    public Fraction subRef(Fraction fraction){
        long numfraction = fraction.num * this.denum;
        this.num *= fraction.denum;
        this.num -= numfraction;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    /**
     * @brief soustraction créant un nouvel objet
     * @param fraction:Fraction
     * @return Fraction
     */
    public Fraction sub(Fraction fraction){
        return this.copy().subRef(fraction);
    }

    /**
     * @brief multiplication modifiant l'objet
     * @param fraction:Fraction
     * @return Fraction
     */
    public Fraction mulRef(Fraction fraction){
        this.num *= fraction.num;
        this.denum *= fraction.denum;
        simplifier();
        return this;
    }

    /**
     * @brief multiplication créant un nouvel objet
     * @param fraction:Fraction
     * @return Fraction
     */
    public Fraction mul(Fraction fraction){
        return this.copy().mulRef(fraction);
    }

    /**
     * @brief multiplication modifiant l'objet
     * @param value:Int
     * @return Fraction
     */
    public Fraction mulRef(int value){
        this.num *= value;
        return this;
    }

    /**
     * @brief multiplication créant un nouvel objet
     * @param value:Int
     * @return Fraction
     */
    public Fraction mul(int value){
        return this.copy().mulRef(value);
    }

    /**
     * @brief division modifiant l'objet
     * @param fraction:Fraction
     * @return Fraction
     * @throws FractionError si division par 0
     */
    public Fraction divRef(Fraction fraction) throws FractionError {
        if (fraction.num == 0)
            throw  new FractionError("Division par zero");
        this.num *= fraction.denum;
        this.denum *= fraction.num;
        simplifier();
        return this;
    }

    /**
     * @brief division créent un nouvel objet
     * @param fraction:Fraction
     * @return Fraction
     * @throws FractionError si division par 0
     */
    public Fraction div(Fraction fraction) throws FractionError {
        return this.copy().divRef(fraction);
    }

    /**
     * @brief division modifiant l'objet
     * @param value:Int
     * @return Fraction
     * @throws FractionError si division par 0
     */
    public Fraction divRef(int value) throws FractionError{
        if (value == 0){
            throw new FractionError("Division par zero");
        }
        this.denum *= value;
        simplifier();
        return this;
    }

    /**
     * @brief division créent un nouvel objet
     * @param value:Int
     * @return Fraction
     * @throws FractionError si division par 0
     */
    public Fraction div(int value) throws FractionError{
        return this.copy().divRef(value);
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

    /**
     * @brief opration ==
     * @param fraction: Fraction
     * @return Booleen
     */
    public boolean equals(Fraction fraction){
        return this.toDouble() == fraction.toDouble();
    }

    /**
     * @brief comparaison -1 si <, 0 si ==, 1 si >
     * @param fraction: Fraction
     * @return Int
     */
    public int compare(Fraction fraction){
        return Double.compare(toDouble(), fraction.toDouble());
    }

    /**
     * @brief Fraction -> Double
     * @return Double
     */
    public double toDouble(){
        return (double) this.num / this.denum;
    }

    /**
     * @brief calcule du pgdc
     *        source : https://stackoverflow.com/questions/4009198/java-get-greatest-common-divisor
     * @param num:Long
     * @param denum:Long
     * @return Long
     */
    private long pgcd(long num, long denum){
        if (denum == 0)
            return num;
        return pgcd(denum, num%denum);
    }

    /**
     * @brief simplifie la fraction
     */
    public void simplifier(){
        long pgcd = pgcd(num,denum);
        num /= pgcd;
        denum /= pgcd;
        checkNeg();
    }

    /**
     * @brief verif les valeurs negatives
     */
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
