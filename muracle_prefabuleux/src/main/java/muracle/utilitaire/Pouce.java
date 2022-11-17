package muracle.utilitaire;

public class Pouce implements java.io.Serializable{

    private int entier;
    private Fraction fraction;

    public Pouce(int entier,int num, int denum) throws FractionError {
        this.entier = entier;
        this.fraction = new Fraction(num,denum);
        if(this.fraction.toDouble() < 0 && this.entier != 0){
            this.entier *= -1;
            this.fraction.mulRef(-1);
        }
    }

    public Pouce(int entier, Fraction fraction){
        this.entier = entier;
        this.fraction = fraction.copy();
    }

    public Pouce(String mesure) throws PouceError,FractionError {
        String[] subString = mesure.trim().split(" ");

        if (subString.length == 2)
            try{
                this.entier = Integer.parseInt(subString[0]);
                String[] fraction = subString[1].split("/");
                this.fraction = new Fraction(Integer.parseInt(fraction[0]),Integer.parseInt(fraction[1]));
            }catch (NumberFormatException ex){
                throw new PouceError("la string d'entré est invalide");
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
                    throw new PouceError("la string d'entré est invalide");

            }catch (NumberFormatException ex){
                throw new PouceError("la string d'entré est invalide");
            }
        else
            throw new PouceError("la string d'entré est invalide");

        if(this.fraction.toDouble() < 0 && this.entier != 0){
            this.entier *= -1;
            this.fraction.mulRef(-1);
        }


    }

    public int getEntier() {
        return entier;
    }

    public void setEntier(int entier) {
        this.entier = entier;
    }

    public Fraction getFraction() {
        return fraction;
    }

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    //opération d'addition par référence
    public Pouce addRef(Pouce pouce) {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.addRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //opération d'addition par référence copy
    public Pouce add(Pouce pouce){
        return this.copy().addRef(pouce);
    }

    public Pouce addRef(int value){
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.addRef(value);
        simplifier();
        return this;
    }

    public Pouce add(int value){
        return this.copy().addRef(value);
    }

    //Opération de soustraction par référence
    public Pouce subRef(Pouce pouce){
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.subRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //Opération de soustraction par référence copy
    public Pouce sub(Pouce pouce){
        return this.copy().subRef(pouce);
    }

    public Pouce subRef(int value){
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.subRef(value);
        simplifier();
        return this;
        //this.entier -= value;
        //simplifier();
        //return this;
    }

    public Pouce sub(int value){
        return this.copy().subRef(value);
    }

    //Oprétation de multiplication par référence
    public Pouce mulRef(Pouce pouce){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.mulRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //Opération de multiplication par référence
    public Pouce mulRef(Fraction fraction){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.mulRef(fraction);
        simplifier();
        return this;
    }

    public Pouce mulRef(int value){
        this.fraction = pouce2Fraction();
        entier = 0;
        this.fraction.mulRef(value);
        simplifier();
        return this;
    }

    //Opération de multiplication par référence copy
    public Pouce mul(Pouce pouce){
        return this.copy().mulRef(pouce);
    }

    //Opération de multiplication par référence copy
    public Pouce mul(Fraction fraction){
        return this.copy().mulRef(fraction);
    }

    public Pouce mul(int value){
        return this.copy().mulRef(value);
    }

    //Opération de division par référence
    public Pouce divRef(Pouce pouce) throws FractionError {
        this.fraction = pouce2Fraction();
        entier = 0;
        fraction.divRef(pouce.pouce2Fraction());
        simplifier();
        return this;
    }

    //Opération de devision par référence copy
    public Pouce div(Pouce pouce) throws FractionError {
        return this.copy().divRef(pouce);
    }

    //opération de division avec un int par référence
    public Pouce divRef(int diviseur) throws PouceError {
        if (diviseur == 0){
            throw new PouceError("Division par 0");
        }
        else {
            this.fraction = pouce2Fraction();
            entier = 0;
            fraction.setDenum(fraction.getDenum() * diviseur);
            simplifier();
            return this;
        }
    }

    //opération de division avec un int par référence copy
    public Pouce div(int diviseur) throws PouceError {
        return this.copy().divRef(diviseur);
    }

    //Opération de ==
    public boolean equals(Pouce pouce){
        return (entier + fraction.toDouble() == pouce.getEntier() + pouce.getFraction().toDouble());
    }

    //Opération de comparaison -1<,0==,1>
    public int compare(Pouce pouce){
        return Double.compare(entier + fraction.toDouble(), pouce.getEntier() + pouce.getFraction().toDouble());
    }

    //Fonction de copy d'objet
    public Pouce copy(){
        return new Pouce(this.entier, this.fraction);
    }

    //Fonction pour simplifier l'entier avec fraction
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

    //Fonction qui formate pouce à Fraction
    private Fraction pouce2Fraction(){
        Fraction fract = this.fraction.copy();
        fract.setNum(fract.getNum() + entier*fract.getDenum());
        return fract;
    }

    public Double toDouble(){
        return pouce2Fraction().toDouble();
    }

    /**
     @brief arroudie la fraction au dénominateur donnée.
     @param value :int la valeur du denum
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
