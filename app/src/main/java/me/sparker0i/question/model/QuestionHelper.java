package me.sparker0i.question.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionHelper {
    public ArrayList<QuestionList> list;
    @SerializedName("cat") private String CAT;

    public String getCAT(){
        return this.CAT;
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

        public String getA() {
            return this.A;
        }

        public String getB() {
            return this.B;
        }

        public String getC() {
            return this.C;
        }

        public String getD() {
            return this.D;
        }

        public String getANS() {
            return this.ANS;
        }
    }
}