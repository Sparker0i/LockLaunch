package me.sparker0i.question.model;

public class Question {
    private String QN;
    private String A;
    private String B;
    private String C;
    private String D;
    private String CAT;
    private String ANS;

    public Question() {

    }

    public Question(String qn , String a , String b , String c , String d , String cat, String ans) {
        QN = qn;
        A = a;
        B = b;
        C = c;
        D = d;
        CAT = cat;
        ANS = ans;
    }

    public String getQN(){
        return this.QN;
    }

    public void setQN(String qn){
        this.QN = qn;
    }

    public String getA(){
        return this.A;
    }

    public void setA(String a){
        this.A = a;
    }

    public String getB(){
        return this.B;
    }

    public void setB(String b){
        this.B = b;
    }

    public String getC(){
        return this.C;
    }

    public void setC(String c){
        this.C = c;
    }

    public String getD(){
        return this.D;
    }

    public void setD(String d){
        this.D = d;
    }

    public String getCAT(){
        return this.CAT;
    }

    public void setCAT(String cat){
        this.CAT = cat;
    }

    public String getANS(){
        return this.ANS;
    }

    public void setANS(String ans){
        this.ANS = ans;
    }
}