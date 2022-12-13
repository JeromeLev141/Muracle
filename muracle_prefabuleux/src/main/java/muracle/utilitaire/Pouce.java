package muracle.utilitaire;

public class Pouce implements java.io.Serializable{

    private int entier;
    private Fraction fraction;

    /**
     * @brief constructeur avec trois int en entré
     * @param entier:int
     * @param num:int
     * @param denum:int
     * @throws FractionError si une fraction par zero est donné
     */
    public Pouce(int entier,int num, int denum) throws FractionError {
        this.entier = entier;
        this.fraction = new Fraction(num,denum);
        if(this.fraction.toDouble() < 0 && this.entier != 0){
            this.entier *= -1;
            this.fraction.mulRef(-1);
        }
    }

    /**
     * @brief constructeur par un int et une fraction
     * @param entier:int
     * @param fraction:Fraction
     */
    public Pouce(int entier, Fraction fraction){
        this.entier = entier;
        this.fraction = fraction.copy();
    }

    /**
     * @brief constructeur par une string
     * @param mesure:String
     * @throws PouceError: si la string est invalide
     * @throws FractionError: si la fraction est sur 0
     */
    public Pouce(String mesure) throws PouceError,FractionError {
        String[] subString = mesure.trim().split(" ");

        if (subString.length == 2)
            try{
                this.entier = Integer.parseInt(subString[0]);
                String[] fraction = subString[1].split("/");
                this.fraction = new Fraction(Integer.parseInt(fraction[0]),Integer.parseInt(fraction[1]));
            }catch (NumberFormatException ex){
                throw new PouceError("la format entré est invalide");
            }

        else if (subString.length == 1)
            try{
                String[] fraction = subString[0].split("/");
                if (fraction.length == 1){
                    this.entier = Integer.parseInt(fraction[0]);
                    this.fraction = new Fraction(0,1);
                }
                else if(fraction.length == 2){
                    this.entier = 0;
                    this.fraction = new Fraction(Integer.parseInt(fraction[0]),Integer.parseInt(fraction[1]));
                }
                else
                    throw new PouceError("la format entré est invalide");

            }catch (NumberFormatException ex){
                throw new PouceError("la format entré est invalide");
            }
        else
            throw new PouceError("la format entré est invalide");

        if(this.fraction.toDouble() < 0 && this.entier != 0){
            this.entier *= -1;
            this.fraction.mulRef(-1);
        }
    }

    /**
     * @brief getteur de l'entier
     * @return this.entier
     */
    public int getEntier() {
        return entier;
    }

    /**
     * @brief setteur de l'entier
     * @param entier:int
     */
    public void setEntier(int entier) {
        this.entier = entier;
    }

    /**
     * @brief getteur de la Fraction
     * @return this.fraction
     */
    public Fraction getFraction() {
        return fraction;
    }

    /**
     * @brief setteur de la fraction
     * @param fraction:fraction
     */
    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    /**
     * @brief addition modifiant l'objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce addRef(Pouce pouce) {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.addRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    /**
     * @brief addition avec un nouvel objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce add(Pouce pouce){
        return this.copy().addRef(pouce);
    }

    /**
     * @brief addition modifiant l'objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce addRef(int value){
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.addRef(value);
        simplifier();
        return this;
    }

    /**
     * @brief addition avec un nouvel l'objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce add(int value){
        return this.copy().addRef(value);
    }

    /**
     * @brief addition modifiant l'objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce addRef(Fraction fraction){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.addRef(fraction);
        simplifier();
        return this;
    }

    /**
     * @brief addition avec un nouvel l'objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce add(Fraction fraction){
        return this.copy().addRef(fraction);
    }

    /**
     * @brief soustraction modifiant l'objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce subRef(Pouce pouce){
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.subRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    /**
     * @brief soustraction créant un nouvel objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce sub(Pouce pouce){
        return this.copy().subRef(pouce);
    }

    /**
     * @brief soustraction modifiant l'objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce subRef(int value){
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.subRef(value);
        simplifier();
        return this;
    }

    /**
     * soustraction créant un nouvel objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce sub(int value){
        return this.copy().subRef(value);
    }

    /**
     * @brief soustraction modifiant l'objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce subRef(Fraction fraction){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.subRef(fraction);
        simplifier();
        return this;
    }

    /**
     * @brief soustraction créant un nouvel objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce sub(Fraction fraction){
        return this.copy().subRef(fraction);
    }

    /**
     * @brief multiplication modifiant l'objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce mulRef(Pouce pouce){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.mulRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    /**
     * @brief multiplication modifiant l'objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce mulRef(Fraction fraction){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.mulRef(fraction);
        simplifier();
        return this;
    }

    /**
     * @brief multiplication modifiant l'objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce mulRef(int value){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.mulRef(value);
        simplifier();
        return this;
    }

    /**
     * @brief multiplication créant un nouvel objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce mul(Pouce pouce){
        return this.copy().mulRef(pouce);
    }

    /**
     * @brief multiplication créant un nouvel objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce mul(Fraction fraction){
        return this.copy().mulRef(fraction);
    }

    /**
     * @brief multiplication créant un nouvel objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce mul(int value){
        return this.copy().mulRef(value);
    }

    /**
     * @brief division modifiant l'objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce divRef(Pouce pouce) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.divRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    /**
     * @brief division créant un nouvel objet
     * @param pouce:Pouce
     * @return Pouce
     */
    public Pouce div(Pouce pouce) throws FractionError {
        return this.copy().divRef(pouce);
    }

    /**
     * @brief division modifiant l'objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce divRef(int value) throws PouceError {
        if (value == 0){
            throw new PouceError("Division par 0");
        }
        else {
            this.fraction = pouce2Fraction();
            entier = 0;
            fraction.setDenum(fraction.getDenum() * value);
            simplifier();
            return this;
        }
    }

    /**
     * @brief division créant un nouvel objet
     * @param value:Int
     * @return Pouce
     */
    public Pouce div(int value) throws PouceError {
        return this.copy().divRef(value);
    }

    /**
     * @brief division modifiant l'objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce divRef(Fraction fraction) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.divRef(fraction);
        simplifier();
        return this;
    }

    /**
     * @brief division créant un nouvel objet
     * @param fraction:Fraction
     * @return Pouce
     */
    public Pouce div(Fraction fraction) throws FractionError {
        return this.copy().divRef(fraction);
    }

    /**
     * @brief racine carré modifiant l'objet actuel
     * @return Pouce
     * @throws PouceError, erreur si pouce est negatif
     */
    public Pouce sqrtRef() throws PouceError {
        if (this.entier < 0)
            throw new PouceError("une racine carré ne peut avoir des chiffre negatife");
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.setNum((long)(Math.sqrt(this.fraction.getNum())*100000000));
        this.fraction.setDenum((long)(Math.sqrt(this.fraction.getDenum())*100000000));
        try {
            this.round(128);
        }catch (FractionError ignored){}
        simplifier();
        return this;
    }

    /**
     * @brief racine carré créant un nouvel objet
     * @return Pouce
     * @throws PouceError, erreur si la valeur de pouce est negatif
     */
    public Pouce sqrt() throws PouceError
    {
        return this.copy().sqrtRef();
    }

    /**
     * @brief operation ==
     * @param pouce:Pouce
     * @return Bool
     */
    public boolean equals(Pouce pouce){
        return (entier + fraction.toDouble() == pouce.getEntier() + pouce.getFraction().toDouble());
    }

    /**
     * @brief comparaison -1 si <, 0 si ==, 1 si >
     * @param pouce:Pouce
     * @return Int
     */
    public int compare(Pouce pouce){
        return Double.compare(entier + fraction.toDouble(), pouce.getEntier() + pouce.getFraction().toDouble());
    }

    /**
     * @brief fait une copy en profondeur de l'objet
     * @return Pouce
     */
    public Pouce copy(){
        return new Pouce(this.entier, this.fraction);
    }

    /**
     * @brief Simplifie la fraction
     */
    public void simplifier(){
        if (this.entier == 0 && this.fraction.toDouble() < 0){
            entier += fraction.getNum()/fraction.getDenum();
            fraction.setNum(fraction.getNum()%fraction.getDenum());
            if (entier != 0)
                fraction.mulRef(-1);
            this.fraction.simplifier();
        }
        else if (this.entier < 0){
            entier -= fraction.getNum()/fraction.getDenum();
            fraction.setNum(fraction.getNum()%fraction.getDenum());
            fraction.simplifier();

            if(this.fraction.toDouble() < 0){
                this.entier *= -1;
                this.fraction.mulRef(-1);
            }
        }
        else{
            entier += fraction.getNum()/fraction.getDenum();
            fraction.setNum(fraction.getNum()%fraction.getDenum());
            fraction.simplifier();

            if(this.fraction.toDouble() < 0){
                this.entier *= -1;
                this.fraction.mulRef(-1);
            }
        }
    }

    /**
     * @brief Pouce -> Fraction
     * @return Fraction
     */
    private Fraction pouce2Fraction(){
        Fraction fract = this.fraction.copy();
        if(entier < 0)
            fract.setNum(entier*fract.getDenum() - fract.getNum());
        else
            fract.setNum(fract.getNum() + entier*fract.getDenum());
        return fract;
    }

    /**
     * @brief tranceforme les pouce sous forme de fraction
     */
    public void formatToFraction(){
        this.fraction = pouce2Fraction();
        this.entier = 0;
    }

    /**
     * @brief Pouce -> Double
     * @return Double
     */
    public Double toDouble(){
        return pouce2Fraction().toDouble();
    }

    /**
     @brief arroudie la fraction au dénominateur donnée.
     @param value :int la valeur du denum
     @throws FractionError: if not valid
     **/
    public Pouce round(int value) throws FractionError {
       this.fraction.round(value);
       simplifier();
       return this;
    }

    @Override
    public String toString() {
        if (fraction.toDouble() == 0)
            return entier + "";
        else if(entier == 0)
            return  fraction.toString();
        else
            return entier + " " + fraction.toString();
    }
}
