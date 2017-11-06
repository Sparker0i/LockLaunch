package me.sparker0i.lock.model;

import java.util.ArrayList;

public class AddModel {
    private String face_id;
    private ArrayList<Images> images = new ArrayList<>();
    public String uploaded_image_url;
    public ArrayList<Errors> Errors;

    public class Images {
        public Attributes attributes;
        public Transaction transaction;
    }

    public class Attributes {
        public String lips;
        public float asian;
        public Gender gender;
        public int age;
        public float hispanic;
        public float other;
        public float black;
        public float white;
        public String glasses;
    }

    public class Gender {
        public float femaleConfidence;
        public String type;
        public float maleConfidence;
    }

    public class Transaction {
        public String status;
        public int topLeftX;
        public int topLeftY;
        public String gallery_name;
        public String timestamp;
        public int height;
        public float quality;
        public float confidence;
        public String subject_id;
        public int eyeDistance;
        public int width;
        public int face_id;
    }

    public class Errors {
        public String Message = "No Error";
        public String ErrCode = "0";
    }
}
