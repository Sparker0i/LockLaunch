package me.sparker0i.question.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UrlModel {

    public ArrayList<UrlList> urls;

    public class UrlList {
        @SerializedName("url") private String url;
        @SerializedName("category") private String cat;

        public String getUrl() {
            return url;
        }

        public String getCategory() {
            return cat;
        }
    }
}
