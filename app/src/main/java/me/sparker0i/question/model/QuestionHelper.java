package me.sparker0i.question.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionHelper {
    public ArrayList<QuestionList> list;
    @SerializedName("cat") private String CAT;

    public String getCAT(){
        return this.CAT;
    }

    public void setCAT(String cat){
        this.CAT = cat;
    }

    public class QuestionList {
        @SerializedName("qn") private String QN;
        @SerializedName("a") private String A;
        @SerializedName("b") private String B;
        @SerializedName("c") private String C;
        @SerializedName("d") private String D;
        @SerializedName("ans") private String ANS;

        public String getQN() {
            return this.QN;
        }

        public void setQN(String qn) {
            this.QN = qn;
        }

        public String getA() {
            return this.A;
        }

        public void setA(String a) {
            this.A = a;
        }

        public String getB() {
            return this.B;
        }

        public void setB(String b) {
            this.B = b;
        }

        public String getC() {
            return this.C;
        }

        public void setC(String c) {
            this.C = c;
        }

        public String getD() {
            return this.D;
        }

        public void setD(String d) {
            this.D = d;
        }

        public String getANS() {
            return this.ANS;
        }

        public void setANS(String ans) {
            this.ANS = ans;
        }
    }
}